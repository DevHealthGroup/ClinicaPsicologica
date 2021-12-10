package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.consultas.*
import android.widget.Toast
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import android.view.MenuItem
import android.widget.EditText
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Entity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.redmadrobot.inputmask.MaskedTextChangedListener
import java.io.FileOutputStream
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/* integra ao armazenamento do banco de dados */
@Entity(tableName = "consultas")
class Consultas: Serializable {

    var idConsulta: String = ""
    var data: String  = ""
    var horario: String  = ""
    var paciente: String  = ""
    var status: String  = ""

    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }
}


class Consulta : AppCompatActivity() {
    val STORAGE_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consultas)

        supportActionBar?.title = "Consultas"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /* cards */
        recyclerConsultas?.layoutManager = LinearLayoutManager(this)
        recyclerConsultas?.itemAnimator = DefaultItemAnimator()
        recyclerConsultas?.setHasFixedSize(true)

        var dataAgendamento = findViewById<EditText>(R.id.agendamento_data)
        var formato = "[00]{/}[00]{/}[0000]"
        var listener = MaskedTextChangedListener(formato, true, dataAgendamento, null, null)

        dataAgendamento.addTextChangedListener(listener)
        dataAgendamento.onFocusChangeListener = listener

        agendar.setOnClickListener {

            var emailPaciente = PreferenceManager.getDefaultSharedPreferences(this)
            var email = emailPaciente.getString("Email", " ")

            var agendamento = Consultas()

            if (agendamento_data.toString().isEmpty()) {
                Toast.makeText(this, "Ops, campos vazios!", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, Consulta::class.java)
                startActivity(intent)
            }
            if (agendamento_horario.toString().isEmpty()) {
                Toast.makeText(this, "Ops, campos vazios!", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, Consulta::class.java)
                startActivity(intent)
            }

            var data = agendamento_data.text.toString()
            var string = StringTokenizer(data, "/")
            var dia = string.nextToken()
            var mes = string.nextToken()
            var ano = string.nextToken()

            agendamento.horario = agendamento_horario.text.toString()
            agendamento.data = "${ano}-${mes}-${dia}"
            agendamento.paciente = email.toString()

            Thread {
                val json = HttpHelper.post("https://devhealthgroup.pythonanywhere.com/consultas/api", agendamento.toJson())
                runOnUiThread{
                    if (json == "True") {
                        Toast.makeText(this, "Consulta agendada com sucesso!", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this, Consulta::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Ops, sua consulta não foi agendada!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()

        }

        // gerar pdf
        bt_geraPdf.setOnClickListener {
            Thread {
                var emailPaciente = PreferenceManager.getDefaultSharedPreferences(this)
                var email = emailPaciente.getString("Email", " ")
                var consultas = ConsultasService.getConsultas(email.toString())
                runOnUiThread {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            val permission =
                                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            requestPermissions(permission, STORAGE_CODE)
                        } else {
                            geraPdf(consultas)
                        }
                    } else {
                        geraPdf(consultas)
                    }
                }
            }.start()
        }
    }

    fun geraPdf(consultas: List<Consultas>) {
        val documento = Document()
        val nomeArquivo = "Relatório de Consultas"
        val diretorio = Environment.getExternalStorageDirectory().toString() + "/" + nomeArquivo +".pdf"

        try {
            PdfWriter.getInstance(documento, FileOutputStream(diretorio))
            documento.open()
            documento.add(Paragraph("Relatório de Consultas"))
            for (c in consultas){
                documento.add(Paragraph("\n\n"))
                documento.add(Paragraph("Data: " + c.data.toString()))
                documento.add(Paragraph("\n"))
                documento.add(Paragraph("Horário: "+ c.horario.toString()))
                documento.add(Paragraph("\n"))
                documento.add(Paragraph("Status: " + c.status.toString()))
            }
            documento.setMargins(2F, 2F, 2F, 2F )
            documento.close()
            Toast.makeText(this, "PDF gerado com sucesso!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception){
            Toast.makeText(this, "Ops, houve um erro ao gerar seu PDF!", Toast.LENGTH_SHORT).show()
        }
    }

    /* ação nos cards */
    fun onclickConsultas(consultas: Consultas) {
        Toast.makeText(this, "Informações da consulta:\n" +
                "Data da consulta: ${consultas.data}\n" +
                "Horário da consulta: ${consultas.horario}\n" +
                "Status da consulta: ${consultas.status}", Toast.LENGTH_SHORT).show()
    }

    // lista de cards
    var consultas = listOf<Consultas>()

    fun taskConsultas() {
        Thread {
            var emailPaciente = PreferenceManager.getDefaultSharedPreferences(this)
            var email = emailPaciente.getString("Email", " ")
            this.consultas = ConsultasService.getConsultas(email.toString())
            runOnUiThread {
                recyclerConsultas?.adapter = ConsultasAdapter(consultas) {onclickConsultas(it)}
                if (this.consultas.isEmpty()) {
                    Toast.makeText(this, "Você não tem consultas agendadas no momento", Toast.LENGTH_SHORT).show()
                } else {
                    enviaNotificacao(consultas.get(0))
                }
            }
        }.start()
    }

    fun enviaNotificacao(consultas: Consultas) {
        // Intent para abrir tela quando clicar na notificação
        val temNotificacaoHabilitada = Prefs.getBoolean("notificacao")

        if(temNotificacaoHabilitada) {
            val intent = Intent(this, Consulta::class.java)
            // parâmetros extras
            intent.putExtra("consultas", consultas)
            // Disparar notificação
            NotificationUtil.create(this, 1, intent, "Consultório Carolina Ramos Bertolino", "Você possui consulta(s) agendada(s)!")
        }
    }

    override fun onResume() {
        super.onResume()
        taskConsultas()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}