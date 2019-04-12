package mx.itesm.taxiunico

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import mx.itesm.taxiunico.listEncuestas.EncuestasActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val encuestasIntent = Intent(this, EncuestasActivity::class.java)
        startActivity(encuestasIntent)

    }
}
