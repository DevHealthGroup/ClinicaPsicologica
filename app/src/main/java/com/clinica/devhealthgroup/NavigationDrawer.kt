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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cadastro -> {
                Toast.makeText(this, "Cadastro", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_consultas -> {
                Toast.makeText(this, "Consultas", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_pagamentos -> {
                Toast.makeText(this, "Pagamentos", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_sair -> {
                finishAffinity()
            }
        }
        layoutMenuLateral.closeDrawer(GravityCompat.START)
        return true
    }
}

