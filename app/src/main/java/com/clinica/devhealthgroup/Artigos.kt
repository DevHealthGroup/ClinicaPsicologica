package com.clinica.devhealthgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.Serializable

class Artigos: Serializable {
    var id: Long? = null
    var nome = ""
    var publicacao = ""
    var foto = ""
    var fonte = ""

}
