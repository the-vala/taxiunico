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


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import mx.itesm.taxiunico.R

class TripConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trip_confirmation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.encuesta, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialog = builder.show()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val confirm = dialogView.findViewById<Button>(R.id.califica)
        confirm.setOnClickListener {
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
            dialog.dismiss()
            Toast.makeText(requireContext(), "Rating: ${ratingBar.rating}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TripConfirmationFragment()
    }
}
