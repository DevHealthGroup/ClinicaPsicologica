package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.login.*
import android.widget.Toast
import android.content.Intent
import androidx.room.Entity
import com.google.gson.GsonBuilder
import java.io.Serializable

// CLASSE USUÁRIO PARA LOGIN
@Entity(tableName = "LoginUser")
class LoginUser: Serializable {

    // CAMPOS DA CLASSE
    var email:String = ""
    var senha:String = ""

    // FUNÇÃO PARA TRANSFORMAR EM JSON
    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }
}

// CLASSE LOGIN
class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DEFINE A TELA DE LOGIN E A IMAGEM QUE APARECE NA TELA
        setContentView(R.layout.login)
        logo_carol_black.setImageResource(R.drawable.logo_carol_black)

        // BUSCA O LOGIN QUE ESTÁ SALVO NAS PREFERÊNCIAS
        email_login.setText(Prefs.getString("usuario_lembrar"))
        senha_login.setText(Prefs.getString("senha_lembrar"))
        checkLembrar.isChecked = Prefs.getBoolean("lembrar_login")

        // QUANDO CLICA EM ENTRAR
        entrar.setOnClickListener{

            // DEFINE AS VARIÁVEIS COM AS INFORMAÇÕES QUE FORAM PASSADAS PELO USUÁRIO
            val email = email_login.text.toString()
            val senha = senha_login.text.toString()
            val checkLogin = checkLembrar.isChecked

            // SALVA O EMAIL E SENHA SE O USUÁRIO MARCAR O CHECKBOX
            if (checkLogin) {
                Prefs.setString("usuario_lembrar", email)
                Prefs.setString("senha_lembrar", senha)
            }
            // NÃO SALVA O EMAIL E SENHA SE O USUÁRIO NÃO MARCAR O CHECKBOX
            else{
                Prefs.setString("usuario_lembrar", "")
                Prefs.setString("senha_lembrar", "")
            }
            Prefs.setBoolean("lembrar_login", checkLogin)

            // DEFINE A INTENT DA CLASSE HOME
            var intent = Intent(this, Home::class.java)

            // CRIA A CLASSE USUÁRIO LOGIN E DEFINE O EMAIL E SENHA
            var user = LoginUser()
            user.email = email
            user.senha = senha

            Thread {
                // ACESSA A API PARA REALIZAR O LOGIN
                var json = HttpHelper.post("https://devhealthgroup.pythonanywhere.com/login/api", user.toJson())
                runOnUiThread{
                    if (json == "True") {
                        // EXIBE UM TOAST DE SUCESSO PARA O USUÁRIO E EXECUTA A INTENT PASSANDO O EMAIL DO USUÁRIO
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        intent.putExtra("Email", email)
                        startActivity(intent);
                    } else {
                        // EXIBE UM TOAST DE FALHA
                        Toast.makeText(this, "Ops, o email ou a senha está incorreto!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()

        }
        // QUANDO CLICAR EM CADASTRAR
        cadastrar.setOnClickListener{
            // CRIA E EXECUTA A INTENT PARA A CLASSE DE CADASTRO
            var intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }
    }
}