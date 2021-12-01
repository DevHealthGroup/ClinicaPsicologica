package com.clinica.devhealthgroup

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import java.io.Serializable


/* integra ao banco de dados */
@Entity(tableName = "usuario")
class Usuario: Serializable {

    @PrimaryKey
    var id: Long? = null
    var nome = ""
    var doc = ""
    var sexo = ""
    var dtNasc = ""
    var cep = ""
    var complemento = ""
    var numero = ""
    var telefone = ""
    var email = ""
    var senha = ""
    var convenio = ""

    override fun toString(): String {
        return "Usuario(nome='$nome')"
    }

    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }
}
