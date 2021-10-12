package com.clinica.devhealthgroup

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.buscar.*
import kotlinx.android.synthetic.main.toolbar.*

class Buscar: NavigationDrawer(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buscar)

        setSupportActionBar(toolbar)

        supportActionBar?.title = "Buscar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configuraMenuLateral()
    }

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