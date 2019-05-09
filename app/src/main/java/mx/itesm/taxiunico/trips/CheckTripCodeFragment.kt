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
package mx.itesm.taxiunico.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_check_trip_code.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.Codes
import mx.itesm.taxiunico.services.CodeService
import mx.itesm.taxiunico.services.Result
import mx.itesm.taxiunico.util.Validator

/**
 * Fragmento para verificar la existencia de un código de reservación
 */
class CheckTripCodeFragment : Fragment() {

    private val codeService = CodeService()
    private var currentJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_check_trip_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscar_button.setOnClickListener {
            verifyCode()
        }
    }

    /**
     * Función para verificar código de reservación
     */
    private fun verifyCode() {
        currentJob = MainScope().launch {
        val reserveCode = editText.text.toString()
           if ( Validator.valReservationCode(reserveCode) ) {
               val result = codeService.getTravelData(editText.text.toString())
               when(result) {
                   is Result.Success ->
                       startTripConfiguration(result.result)
               }
           } else {
                Toast.makeText(context,"Código inválido", Toast.LENGTH_SHORT).show()
           }
        }
    }

    /**
     * Función para establecer los parametros del viaje
     */
    private fun startTripConfiguration(code: Codes) {
        requireFragmentManager().beginTransaction()
            .replace(
                android.R.id.content,
                TripConfigurationFragment.newInstance(code)
            )
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onPause() {
        super.onPause()
        currentJob?.cancel()
    }
}
