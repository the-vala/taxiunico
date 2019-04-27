package mx.itesm.taxiunico.listEncuestas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_encuestas.*
import mx.itesm.taxiunico.R

class EncuestasActivity : AppCompatActivity() {

    lateinit var adapter: EncuestaAdapter
    private var encuestas: MutableList<Encuesta> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuestas)

        updateData()
    }

    private fun updateData () {
        encuestas = Encuesta.loadEncuestas()
        adapter.updateAdapter(encuestas)
    }
}
