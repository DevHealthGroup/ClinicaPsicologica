package com.clinica.devhealthgroup

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArtigosDAO {

    @Query("SELECT * FROM artigos WHERE id=:id")
    fun getById(id: Long): Artigos?

    @Query("SELECT * FROM artigos")
    fun findAll(): List<Artigos>

    @Insert
    fun insert(artigo: Artigos)

    @Delete
    fun delete(artigo: Artigos)
}