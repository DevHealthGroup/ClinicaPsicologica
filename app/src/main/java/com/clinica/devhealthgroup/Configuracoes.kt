package com.clinica.devhealthgroup

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.configuracoes.*


class Configuracoes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuracoes)

        supportActionBar?.title = "Configurações"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupNotificationSwitch()
    }

    private fun setupNotificationSwitch() {
        switch2.isChecked = Prefs.getBoolean("notificacao")

        switch2.setOnCheckedChangeListener { _, isChecked ->
            Prefs.setBoolean("notificacao", isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
