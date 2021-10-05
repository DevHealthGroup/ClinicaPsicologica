package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.toolbar.*
import android.content.Intent

class Home: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        setSupportActionBar(toolbar)

        supportActionBar?.title = "Home"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            var intent = Intent(this, Configurações::class.java)
            startActivity(intent)

        } else if (id == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}