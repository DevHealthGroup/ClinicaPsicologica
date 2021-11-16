package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.login.*
import android.widget.Toast
import android.content.Intent
import android.util.Log
import android.view.View


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        logo_carol_black.setImageResource(R.drawable.logo_carol_black)

        usuario.setText(Prefs.getString("usuario_lembrar"))
        senha.setText(Prefs.getString("senha_lembrar"))
        checkLembrar.isChecked = Prefs.getBoolean("lembrar_login")

        entrar.setOnClickListener{
            val usuario = usuario.text.toString()
            val senha = senha.text.toString()
            val check_login = checkLembrar.isChecked

            if (check_login) {
                Prefs.setString("usuario_lembrar", usuario)
                Prefs.setString("senha_lembrar", senha)
            } else{
                Prefs.setString("usuario_lembrar", "")
                Prefs.setString("senha_lembrar", "")
            }
            Prefs.setBoolean("lembrar_login", check_login)

            var intent = Intent(this, Home::class.java)

            if (usuario == "aluno" && senha == "impacta") {
                startActivity(intent)
            } else {
                Toast.makeText(this,"Login incorreto", Toast.LENGTH_LONG).show()
            }
        }
    }
}