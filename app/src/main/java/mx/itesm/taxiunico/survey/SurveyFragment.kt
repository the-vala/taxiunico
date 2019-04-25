package mx.itesm.taxiunico.survey

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mx.itesm.taxiunico.R
import kotlinx.android.synthetic.main.dialog_survey.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class SurveyDialogFragment : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    val surveyService = SurveyService()
    //val auth = FirebaseAuth.getInstance()
    //TODO implementar auth de Driver
    val idDriver = "%G2TY35RDG5S45"
    private var score: Int = 5

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val inflater = activity!!.layoutInflater
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(inflater.inflate(R.layout.dialog_survey, null))

        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        star1.setOnClickListener {updateScore(1)}
        star2.setOnClickListener {updateScore(2)}
        star3.setOnClickListener {updateScore(3)}
        star4.setOnClickListener {updateScore(4)}
        star5.setOnClickListener {updateScore(5)}

        saveSurvey.setOnClickListener {saveSurvey()}

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun updateScore(newScore: Int) {
        score = newScore
    }

    private fun saveSurvey() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        MainScope().launch {
            surveyService.addSurvey(idDriver, Survey(
                score,
                currentDate
            ))
        }
    }

}