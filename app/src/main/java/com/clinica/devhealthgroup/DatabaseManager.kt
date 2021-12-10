package com.clinica.devhealthgroup

import androidx.room.Room

/* banco de dados local com room - sq lite */
object DatabaseManager {
    private var dbInstance: DevHealthGroup

    init {
        val context = DHGApplication.getInstance().applicationContext
        dbInstance = Room.databaseBuilder(
            context,
            DevHealthGroup::class.java,
            "dhg.sqlite"
        ).build()
    }

    fun getUsuariosDAO(): UsuariosDAO {
        return dbInstance.usuariosDAO()
    }
}