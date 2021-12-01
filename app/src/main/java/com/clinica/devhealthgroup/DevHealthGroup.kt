package com.clinica.devhealthgroup

import androidx.room.Database
import androidx.room.RoomDatabase

/* banco de dados local com room */
@Database(entities = arrayOf(Artigos::class), version = 1)
abstract class DevHealthGroup: RoomDatabase() {
    abstract fun artigosDAO() : ArtigosDAO
}