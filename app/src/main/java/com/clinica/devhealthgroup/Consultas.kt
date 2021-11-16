package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.consultas.*
import android.widget.Toast
import android.content.Intent
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
}