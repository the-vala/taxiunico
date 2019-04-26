package mx.itesm.taxiunico.survey

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import mx.itesm.taxiunico.R
import kotlinx.android.synthetic.main.fragment_survey.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SurveyFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    val surveyService = SurveyService()
    //val auth = FirebaseAuth.getInstance()
    //TODO implementar auth de Driver
    val idDriver = "%G2TY35RDG5S45"
    private var score: Int = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_survey, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        star1.setOnClickListener {
            restoreButtons()
            star1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selectedScore))
            updateScore(1)}
        star2.setOnClickListener {
            restoreButtons()
            star2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selectedScore))
            updateScore(2)}
        star3.setOnClickListener {
            restoreButtons()
            star3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selectedScore))
            updateScore(3)}
        star4.setOnClickListener {
            restoreButtons()
            star4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selectedScore))
            updateScore(4)}
        star5.setOnClickListener {
            restoreButtons()
            star5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selectedScore))
            updateScore(5)}

        saveSurvey.setOnClickListener {saveSurvey()}
    }

    private fun restoreButtons() {
        star1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.taxiYellow))
        star2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.taxiYellow))
        star3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.taxiYellow))
        star4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.taxiYellow))
        star5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.taxiYellow))
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