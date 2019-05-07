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
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_survey_list.*
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.services.SurveyService

class SurveyListFragment : Fragment() {
    private val surveyService = SurveyService()
    //TODO implement auth for driver
    private val auth = FirebaseAuth.getInstance()
    val driverID = "%G2TY35RDG5S45"
    //
    private var encuestas: MutableList<Survey> = mutableListOf()
    lateinit var adapter: SurveyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_survey_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SurveyAdapter(requireContext(), encuestas)

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
