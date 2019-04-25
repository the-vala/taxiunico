package mx.itesm.taxiunico.survey

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import mx.itesm.taxiunico.R

class SurveysActivity : AppCompatActivity() {

    lateinit var adapter: SurveyAdapter
    private var encuestas: MutableList<Survey> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuestas)

        updateData()
    }

    private fun updateData () {
        encuestas = Survey.loadEncuestas()
        adapter.updateAdapter(encuestas)
    }
}
