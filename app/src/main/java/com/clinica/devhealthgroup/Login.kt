package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.login.*
import android.widget.Toast
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import java.io.Serializable

//@Entity(tableName = "LoginUser")
//class LoginUser: Serializable {

//    var email = ""
//    var senha = ""

//    fun toJson(): String {
//        return GsonBuilder().create().toJson(this)
//    }
//}


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        logo_carol_black.setImageResource(R.drawable.logo_carol_black)

        email_login.setText(Prefs.getString("usuario_lembrar"))
        senha_login.setText(Prefs.getString("senha_lembrar"))
        checkLembrar.isChecked = Prefs.getBoolean("lembrar_login")

        entrar.setOnClickListener{
            val email = email_login.text.toString()
            val senha = senha_login.text.toString()
            val checkLogin = checkLembrar.isChecked

            /* salva no campo editável login e senha quando seleciona o checkbox*/
            if (checkLogin) {
                Prefs.setString("usuario_lembrar", email)
                Prefs.setString("senha_lembrar", senha)
            } else{
                Prefs.setString("usuario_lembrar", "")
                Prefs.setString("senha_lembrar", "")
            }
            Prefs.setBoolean("lembrar_login", checkLogin)

            var intent = Intent(this, Home::class.java)

            //Thread {
            //    var user = LoginUser()
            //    user.email = email_login.text.toString()
            //    user.senha = senha_login.text.toString()

            //   Log.d("email", user.email)
            //    Log.d("email", user.email)

            //     var json = HttpHelper.post("https://devhealthgroup.pythonanywhere.com/login/api", user.toJson())
            //}.start()

            /* acesso quando confirma login e senha definido */
            if (email == "aluno" && senha == "impacta") {
                startActivity(intent)
            }
        }
        /* direciona para a activity após ação do botão em questão */
        cadastrar.setOnClickListener{
            var intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }
    }
}