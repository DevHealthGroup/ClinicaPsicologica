package com.clinica.devhealthgroup

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.toolbar.*

// CLASSE HOME
class Home : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DEFINE A TELA HOME, ADICIONA A ACTIONBAR PASSANDO O NOME DA TELA E CONFIGURA O MENU LATERAL
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configuraMenuLateral()

        // PEGA O EMAIL DO USUÁRIO DA INTENT E ADICIONA NO SHAREDPREFERENCES
        val email = intent.getStringExtra("Email")
        var emailPaciente = PreferenceManager.getDefaultSharedPreferences(this)
        emailPaciente.edit().putString("Email", email).apply()
    }

    // DEFINE O CONTEXTO
    val context = this

    // CRIA OS ITENS DO MENU LATERAL
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // buscar
        (menu?.findItem(R.id.action_buscar)?.actionView as SearchView). setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(context, "Buscar ${query}", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }

    // cria opções ation bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_config) {
            var intent = Intent(this, Configuracoes::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
