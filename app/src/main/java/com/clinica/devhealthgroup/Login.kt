package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.login.*
import android.widget.Toast
import android.content.Intent



class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        logo_carol_black.setImageResource(R.drawable.logo_carol_black)

        entrar.setOnClickListener{
            val usuario = usuario.text.toString()
            val senha = senha.text.toString()

            var intent = Intent(this, Home::class.java)

            if (usuario == "aluno" && senha == "impacta") {
                startActivity(intent)
            } else {
                Toast.makeText(this,"Login incorreto", Toast.LENGTH_LONG).show()
            }
        }
    }
}