package com.clinica.devhealthgroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.toolbar.*


/* configura menu lateral */
open class NavigationDrawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun configuraMenuLateral() {
        val toogle = ActionBarDrawerToggle(
            this,
            layoutMenuLateral,
            toolbar,
            R.string.abrir,
            R.string.fechar
        )
        layoutMenuLateral.addDrawerListener(toogle)
        toogle.syncState()
        nav_menu_lateral.setNavigationItemSelectedListener(this)
    }

    /* opçoes de menu e direcionamento */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
            R.id.nav_consultas -> {
                val intent = Intent(this, Consultas::class.java)
                startActivity(intent)
            }
            R.id.nav_agendamentos -> {
                val intent = Intent(this, Agendamento::class.java)
                startActivity(intent)
            }
            R.id.nav_pagamentos -> {
                val intent = Intent(this, Pagamentos::class.java)
                startActivity(intent)
            }
            R.id.nav_localizacao -> {
                val intent = Intent(this, Mapas::class.java)
                startActivity(intent)
            }
            R.id.nav_sair -> {
                finishAffinity()
            }
        }
        layoutMenuLateral.closeDrawer(GravityCompat.START)
        return true
    }
}

