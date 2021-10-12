package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context

object ArtigosService {

    fun getArtigos(context: Context): List<Artigos> {
        val artigos = mutableListOf<Artigos>()

        for (i in 1..4) {
            var d = Artigos()
            d.nome = "Artigos $i"
            d.publicacao = "00/00/00"
            d.fonte = "Fonte $i"
            d.foto = "https://image.flaticon.com/icons/png/512/1056/1056496.png"
            artigos.add(d)
        }

        return artigos
    }
}
