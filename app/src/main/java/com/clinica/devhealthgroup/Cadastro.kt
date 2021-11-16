package com.clinica.devhealthgroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.cadastro.*
import kotlinx.android.synthetic.main.toolbar.*

class Cadastro : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Cadastro"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configuraMenuLateral()
        nav_menu_lateral.setCheckedItem(R.id.nav_cadastro)

        cadastrar.setOnClickListener {
            var art = Artigos()
            art.nome = nome.text.toString()
            art.publicacao = doc.text.toString()
            art.fonte = email.text.toString()
            art.foto = "https://image.flaticon.com/icons/png/512/1056/1056496.png"

            Thread {
                saveOffline(art)
                runOnUiThread {
                    finish()
                }
            }.start()

            var intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    fun saveOffline(art: Artigos): Boolean {
        val dao = DatabaseManager.getArtigosDAO()
        dao.insert(art)
        return true
    }
}