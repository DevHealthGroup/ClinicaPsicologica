package com.clinica.devhealthgroup

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/*lista cards*/
object PagamentosService {
    fun getPagamentos(email: String): List<Pagamentos> {
        var url = "https://devhealthgroup.pythonanywhere.com/data/pagamentos/98765432135715924687/${email}"
        var json = HttpHelper.get(url)
        var pagamentos = parserJson<MutableList<Pagamentos>>(json)
        var pagamentosAll = mutableListOf<Pagamentos>()
        for (i in pagamentos) {
            pagamentosAll.add(i)
        }
        return pagamentosAll
    }

    inline fun <reified T> parserJson(json: String): T {
        val type = object : TypeToken<T>(){}.type
        return Gson().fromJson<T>(json, type)
    }
}
