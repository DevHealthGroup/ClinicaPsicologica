package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class Mapas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapas)

        supportActionBar?.title = "Clínica Carolina Ramos Bertolino"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /* utiliza on resume pois pode demorar a carregar */
    override fun onResume() {
        super.onResume()
        val mapaFragment = MapaFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.layoutMapas, mapaFragment)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

