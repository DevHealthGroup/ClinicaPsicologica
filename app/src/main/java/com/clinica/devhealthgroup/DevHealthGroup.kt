package com.clinica.devhealthgroup

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Artigos::class), version = 1)
abstract class DevHealthGroup: RoomDatabase() {
    abstract fun artigosDAO() : ArtigosDAO
}