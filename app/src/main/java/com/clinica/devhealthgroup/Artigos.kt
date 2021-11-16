package com.clinica.devhealthgroup

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "artigos")
class Artigos: Serializable {

    @PrimaryKey
    var id: Long? = null
    var nome = ""
    var publicacao = ""
    var foto = ""
    var fonte = ""
}
