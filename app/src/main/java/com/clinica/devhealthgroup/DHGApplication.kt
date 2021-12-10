package com.clinica.devhealthgroup

import android.app.Application

class DHGApplication : Application() {

    /* chama ao iniciar o processo da aplicação */
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: DHGApplication? = null

        fun getInstance(): DHGApplication {
            if (appInstance == null) {
                throw IllegalStateException("Configurar application no Manifest")
            }
            return appInstance!!
        }
    }

}