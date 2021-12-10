package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.pagamentos.*
import android.widget.Toast
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Entity
import com.google.gson.GsonBuilder
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.io.Serializable

/* integra ao armazenamento do banco de dados */
@Entity(tableName = "pagamentos")
class Pagamentos: Serializable {

    var idPagamento: String = ""
    var consulta: String  = ""
    var paciente: String  = ""
    var valor: String  = ""
    var status: String  = ""
    var convenio: String = ""

    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }
}


class Pagamento : AppCompatActivity() {

    val STORAGE_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pagamentos)

        supportActionBar?.title = "Pagamentos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        recyclerPagamentos?.layoutManager = LinearLayoutManager(this)
        recyclerPagamentos?.itemAnimator = DefaultItemAnimator()
        recyclerPagamentos?.setHasFixedSize(true)

        // gerar pdf
        bt_geraPdf.setOnClickListener {
            Thread {
                var emailPaciente = PreferenceManager.getDefaultSharedPreferences(this)
                var email = emailPaciente.getString("Email", " ")
                var pagamentos = PagamentosService.getPagamentos(email.toString())
                runOnUiThread {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            val permission =
                                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            requestPermissions(permission, STORAGE_CODE)
                        } else {
                            geraPdf(pagamentos)
                        }
                    } else {
                        geraPdf(pagamentos)
                    }
                }
            }.start()
        }
    }


    /* ação nos cards */
    fun onClickPagamentos(pagamentos: Pagamentos) {
        Toast.makeText(this, "Informações:\n" +
                "Status: ${pagamentos.status}\n" +
                "Id da consulta: ${pagamentos.consulta}\n" +
                "Valor: ${pagamentos.valor}", Toast.LENGTH_SHORT).show()
    }
    /* lista de cards*/
    var pagamentos = listOf<Pagamentos>()

    fun taskPagamentos() {
        Thread {
            var emailPaciente = PreferenceManager.getDefaultSharedPreferences(this)
            var email = emailPaciente.getString("Email", " ")
            this.pagamentos = PagamentosService.getPagamentos(email.toString())
            runOnUiThread {
                recyclerPagamentos?.adapter = PagamentosAdapter(pagamentos) {onClickPagamentos(it)}
            }
        }.start()
    }

//gerar pdf
fun geraPdf(pagamentos: List<Pagamentos>) {
    val documento = Document()
    val nomeArquivo = "Relatório Financeiro"
    val diretorio = Environment.getExternalStorageDirectory().toString() + "/" + nomeArquivo +".pdf"

    try {
        PdfWriter.getInstance(documento, FileOutputStream(diretorio))
        documento.open()
        documento.add(Paragraph("Relatório de Financeiro"))
        for (c in pagamentos){
            documento.add(Paragraph("\n\n"))
            documento.add(Paragraph("Valor: " + c.valor.toString()))
            documento.add(Paragraph("\n"))
            documento.add(Paragraph("Paciente: "+ c.paciente.toString()))
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

    override fun onResume() {
        super.onResume()
        taskPagamentos()
    }

    /* icone de retorno a activity apontada */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}