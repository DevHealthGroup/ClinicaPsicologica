package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.cadastro.*
import kotlinx.android.synthetic.main.toolbar.*

class Cadastro : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)
        setSupportActionBar(toolbar)
        nav_menu_lateral.setCheckedItem(R.id.nav_cadastro)
        configuraMenuLateral()
    }
}