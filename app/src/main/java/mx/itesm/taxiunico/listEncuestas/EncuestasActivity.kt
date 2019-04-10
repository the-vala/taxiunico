package mx.itesm.taxiunico

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_encuestas.*

class EncuestasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuestas)

        if (encuestas.isEmpty()) {
            encuestas = Encuesta.loadEncuestas()
        }
        updateAdapter(encuestas)

    }

    private fun updateAdapter(mutableListBooks: MutableList<Encuesta>) {
        val adapter = EncuestaAdapter(this, mutableListBooks)
        adapter.notifyDataSetChanged()
        list_encuestas.adapter = adapter
    }

    companion object {
        var encuestas =  mutableListOf<Encuesta>()
    }
}
