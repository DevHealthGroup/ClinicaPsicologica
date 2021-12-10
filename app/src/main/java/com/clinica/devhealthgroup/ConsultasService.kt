package com.clinica.devhealthgroup

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/*lista cards*/
object ConsultasService {
    fun getConsultas(email: String): List<Consultas> {
        var url = "https://devhealthgroup.pythonanywhere.com/data/consultas/3219876547531596482/${email}"
        var json = HttpHelper.get(url)
        var consultas = parserJson<MutableList<Consultas>>(json)
        var consultasAll = mutableListOf<Consultas>()
        for (i in consultas) {
            consultasAll.add(i)
        }
        return consultasAll
    }

    inline fun <reified T> parserJson(json: String): T {
        val type = object : TypeToken<T>(){}.type
        return Gson().fromJson<T>(json, type)
    }
}
