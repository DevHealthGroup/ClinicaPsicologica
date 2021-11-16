package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer
import java.net.URL

object ArtigosService {

    fun getArtigos(context: Context): List<Artigos> {
        val artigos1 = mutableListOf<Artigos>()
        val dao = DatabaseManager.getArtigosDAO()
        for (i in dao.findAll()) {
            artigos1.add(i)
        }
        saveArtigos()
        return artigos1
    }

    fun saveArtigos(): Boolean {
        val url = "https://fabiomvieira.pythonanywhere.com/artigos"
        var json = HttpHelper.get(url)
        var artigos = parserJson<MutableList<Artigos>>(json)
        for (i in artigos) {
            Thread {
                val dao = DatabaseManager.getArtigosDAO()
                dao.insert(i)
            }.start()
        }
        return true
    }

    inline fun <reified T> parserJson(json: String): T {
        val type = object : TypeToken<T>(){}.type
        return Gson().fromJson<T>(json, type)
    }
}
