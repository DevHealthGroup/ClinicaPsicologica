package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.consultas.*
import android.widget.Toast
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.toolbar.*

class Consultas : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consultas)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Consultas"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configuraMenuLateral()

        nav_menu_lateral.setCheckedItem(R.id.nav_consultas)
    }
    /* menu - action bar */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_buscar) {
            var intent = Intent(this, Buscar::class.java)
            startActivity(intent)

        } else if (id == R.id.action_config) {
            var intent = Intent(this, Configuracoes::class.java)
            startActivity(intent)

        } else if (id == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
