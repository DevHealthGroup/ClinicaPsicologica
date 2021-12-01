package com.clinica.devhealthgroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home.layoutMenuLateral
import kotlinx.android.synthetic.main.home.nav_menu_lateral
import kotlinx.android.synthetic.main.toolbar.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager


class Home : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        /* menu - action bar e navigation drawer*/
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Home"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        configuraMenuLateral()

        /* cards */
        recyclerArtigos?.layoutManager = LinearLayoutManager(this)
        recyclerArtigos?.itemAnimator = DefaultItemAnimator()
        recyclerArtigos?.setHasFixedSize(true)
    }


    fun enviaNotificacao(artigos: Artigos) {
        // Intent para abrir tela quando clicar na notificação
        val intent = Intent(this, Configuracoes::class.java)
        // parâmetros extras
        intent.putExtra("artigos", artigos)
        // Disparar notificação
        NotificationUtil.create(this, 1, intent, "Dev Health Group", "Você tem um novo artigo ${artigos.nome}")
    }

    /* ação nos cards */
    fun onclickArtigos(artigos: Artigos) {
        Toast.makeText(this, "Clicou no  ${artigos.nome} - Publicado em ${artigos.publicacao} -  ${artigos.fonte}", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, Home::class.java)
        intent.putExtra("artigos", artigos)
        startActivity(intent)
    }
    /* lista de cards*/
    var artigos = listOf<Artigos>()

    fun taskArtigos() {
        Thread {
            this.artigos = ArtigosService.getArtigos(this)
            runOnUiThread {
                recyclerArtigos?.adapter = ArtigosAdapter(artigos) {onclickArtigos(it)}
                enviaNotificacao(this.artigos.get(4))
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        taskArtigos()
    }

    val context = this

    /* opcao buscar - action bar */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

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

    /* menu - action bar */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_buscar) {
            var intent = Intent(this, Buscar::class.java)
            startActivity(intent)

        } else if (id == R.id.action_config) {
            var intent = Intent(this, Configuracoes::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
