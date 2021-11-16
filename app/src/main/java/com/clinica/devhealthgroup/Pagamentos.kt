package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.pagamentos.*
import android.widget.Toast
import android.content.Intent
import kotlinx.android.synthetic.main.toolbar.*

class Pagamentos : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pagamentos)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Pagamentos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configuraMenuLateral()
        nav_menu_lateral.setCheckedItem(R.id.nav_pagamentos)
    }
}