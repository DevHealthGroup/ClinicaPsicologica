package com.clinica.devhealthgroup

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {
    //val TAG = "firebase"
    // recebe o novo token criado
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d("DHGFirebase", "Novo token: $token")
        // guarda token em SharedPreferences
        Prefs.setString("FB_TOKEN", token!!)
    }

    // recebe a notificação de push
    override fun onMessageReceived(mensagemRemota: RemoteMessage?) {
        Log.d("DHGFirebase", "onMessageReceived")

        // verifica se a mensagem recebida do firebase é uma notificação
        if (mensagemRemota?.notification != null) {
            val titulo = mensagemRemota?.notification?.title
            var corpo = mensagemRemota?.notification?.body
            Log.d("DHGFirebase", "Titulo da mensagem: $titulo")
            Log.d("DHGFirebase", "Corpo da mensagem: $corpo")

            if (mensagemRemota?.data.isNotEmpty()) {
                val artigosId = mensagemRemota.data.get("artigosId")
                corpo += " $artigosId"
            }

            val intent = Intent(this, Consultas::class.java)

            NotificationUtil.create(this, 1, intent, titulo!!, corpo!!)
        }

    }
}
            /*showNotification(mensagemRemota)
        }
    }

    private fun showNotification(mensagemRemota: RemoteMessage) {

        // Intent para abrir quando clicar na notificação
        val intent = Intent(this, Artigos::class.java)
        // se título for nulo, utilizar nome no app
        val titulo = mensagemRemota?.notification?.title?: getString(R.string.app_name)
        var mensagem = mensagemRemota?.notification?.body!!

        // verificar se existem dados enviados no push
        if(mensagemRemota?.data.isNotEmpty()) {
            val artigosId = mensagemRemota.data.get("artigosId")?.toLong()!!
            mensagem += ""
            // recuperar artigo no WS
            val artigos = ArtigosService.getArtigos(this, artigosId)
            intent.putExtra("artigos", artigos)
        }
        NotificationUtil.create(this, 1, intent, titulo, mensagem)
    }
}*/