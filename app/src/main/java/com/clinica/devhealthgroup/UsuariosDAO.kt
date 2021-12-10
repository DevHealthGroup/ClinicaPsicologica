package com.clinica.devhealthgroup

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/* utilização do banco local */
@Dao
interface UsuariosDAO {

    @Query("SELECT * FROM usuario")
    fun findAll(): List<Usuario>

    @Insert
    fun insert(usuario: Usuario)

    @Delete
    fun delete(usuario: Usuario)
}