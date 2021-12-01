package com.clinica.devhealthgroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.agendamento.*
import kotlinx.android.synthetic.main.cadastro.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.Serializable

@Entity(tableName = "agendamento")
class AgendamentoAPI: Serializable {

    @PrimaryKey
    var paciente = 2
    var hora = ""
    var dia = ""
    var status = ""

    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }
}


class Agendamento : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agendamento)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Agendamento"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configuraMenuLateral()
        nav_menu_lateral.setCheckedItem(R.id.nav_agendamentos)

        agendar.setOnClickListener {

            var agendamento = AgendamentoAPI()
            agendamento.hora = agendamento_horario.text.toString()
            agendamento.dia = agendamento_data.text.toString()

            Thread {
                val json = HttpHelper.post("https://devhealthgroup.pythonanywhere.com/consultas/api", agendamento.toJson())
            }.start()
            var intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}