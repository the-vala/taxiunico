package mx.itesm.taxiunico.survey

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_encuestas.*
import mx.itesm.taxiunico.R

class SurveysActivity : AppCompatActivity() {
    private val surveyService = SurveyService()
    //TODO implement auth for driver
    private val auth = FirebaseAuth.getInstance()
    val driverID = "%G2TY35RDG5S45"
    //
    private var encuestas: MutableList<Survey> = mutableListOf()
    private var adapter: SurveyAdapter = SurveyAdapter(this, encuestas)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuestas)

        list_encuestas.adapter = adapter
        updateData()
    }

    private fun updateData () {
        surveyService.getSurveys(
            driverID,
            onComplete = ::updateSurveys
        )
    }

    private fun updateSurveys(newList: MutableList<Survey>) {
        adapter.updateAdapter(newList)

    }
}
