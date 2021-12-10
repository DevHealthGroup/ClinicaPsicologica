package com.clinica.devhealthgroup

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.cadastro.*
import android.view.MenuItem
import android.widget.Toast

// CLASSE DE CADASTRO
class Cadastro : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DEFINE A TELA DE CADASTRO E ADICIONA A ACTIONBAR PASSANDO O NOME DA TELA
        setContentView(R.layout.cadastro)
        supportActionBar?.title = "Cadastro"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // QUANDO CLICA EM CADASTRAR
        cadastrar.setOnClickListener {

            // CRIA UMA CLASSE USUÁRIO E DEFINE TODOS OS CAMPOS DE ACORDO COM O QUE O USUÁRIO PASSOU
            var user = Usuario()
            user.nome = cadastro_nome.text.toString()
            user.doc = cadastro_doc.text.toString()
            user.sexo = cadastro_sexo.text.toString()
            user.dtNasc = cadastro_dtNasc.text.toString()
            user.cep = cadastro_cep.text.toString()
            user.numero = cadastro_numero.text.toString()
            user.telefone = cadastro_telefone.text.toString()
            user.email = cadastro_email.text.toString()
            user.senha = cadastro_senha.text.toString()
            user.convenio = "0"

            Thread {
                // ACESSA A API DE LOGIN
                val json = HttpHelper.post("https://devhealthgroup.pythonanywhere.com/cadastro/api", user.toJson())
                runOnUiThread{
                    if (json == "True") {
                        // EXIBE UM TOAST DE SUCESSO PARA O USUÁRIO, CRIA E EXECUTA A INTENT PARA LOGIN
                        Toast.makeText(this, "Cadastro concluído com sucesso!", Toast.LENGTH_LONG).show()
                        var intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    } else {
                        // EXIBE UM TOAST DE FALHA
                        Toast.makeText(this, "Ops, os dados inseridos são inválidos!", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        }
    }

    // CRIA AS OPÇÕES DA ACTIONBAR
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}