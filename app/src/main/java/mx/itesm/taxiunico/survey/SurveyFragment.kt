/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.itesm.taxiunico.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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