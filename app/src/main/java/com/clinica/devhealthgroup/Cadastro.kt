package com.clinica.devhealthgroup

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.cadastro.*
import android.util.Log



class Cadastro : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)

        /* dados a serem salvos na ação de clicar no botão em questão*/
        cadastrar.setOnClickListener {
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

            /* definição da api */
            Thread {
                val json = HttpHelper.post("https://devhealthgroup.pythonanywhere.com/cadastro/api", user.toJson())
            }.start()

            /* direcionamento de activity */
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}