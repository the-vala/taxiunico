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
package mx.itesm.taxiunico.travels

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.models.UserType
import java.io.IOException
import android.content.Intent
import android.net.Uri
import android.widget.RatingBar
import mx.itesm.taxiunico.models.TripStatus
import mx.itesm.taxiunico.models.Viaje
import mx.itesm.taxiunico.services.TripService

class PendingTripsFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var adapter: ViajeAdapter
    private lateinit var authService: AuthService
    private lateinit var tripService: TripService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authService = AuthService(requireContext())
        tripService = TripService()

        //Load pending trips
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        adapter = ViajeAdapter(mutableListOf(), authService)
        recyclerView.adapter = adapter

        //If user is driver make item clickable
        if (authService.getUserType() == UserType.DRIVER) {
            adapter.onItemClick = { data -> createConfirmTripDialog(data)}
        }
        else if (authService.getUserType() == UserType.TRAVELER) {
            adapter.onItemClick = { data -> createCancelTripDialog(data) }
        }
    }

    override fun onResume() {
        super.onResume()
        updateData()
    }

    private fun updateData() {
        MainScope().launch {
            var viajes = TripService().getTravelHistory(auth.uid!!)
            viajes = viajes.filter{ it.second.status == TripStatus.PENDING }.toMutableList()
            adapter.setData(viajes)
        }
    }

    private fun createConfirmTripDialog(data: Pair<String, Viaje>) {
        val viaje = data.second
        //Open alertDialog to start trip
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_driver_trip_confirmation, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialog = builder.show()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Get origin and destination address
        val ori = dialogView.findViewById<TextView>(R.id.confirmationOri)
        val des = dialogView.findViewById<TextView>(R.id.confirmationDes)
        var geocodeMatchesOri: List<Address>? = null
        var geocodeMatchesDes: List<Address>? = null
        val AddressOri: String?
        val AddressDes: String?

        try {
            geocodeMatchesOri = Geocoder(requireContext())
                .getFromLocation(
                    viaje.origin.latitude,
                    viaje.origin.longitude,
                    1
                )
            geocodeMatchesDes = Geocoder(requireContext())
                .getFromLocation(
                    viaje.destination.latitude,
                    viaje.destination.longitude,
                    1
                )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (geocodeMatchesOri != null && geocodeMatchesDes != null) {
            AddressOri = geocodeMatchesOri[0].getAddressLine(0)
            ori.text = AddressOri.toString()
            AddressDes = geocodeMatchesDes[0].getAddressLine(0)
            des.text = AddressDes.toString()
        }

        //Get trip start time and client info
        var dateTime = dialogView.findViewById<TextView>(R.id.confirmationDateTime)
        dateTime.text = viaje.dateTime.toDate().toString()
        var cliente = dialogView.findViewById<TextView>(R.id.name)
        cliente.text = viaje.userName

        //If trip accepted
        var confirm = dialogView.findViewById<Button>(R.id.confirm)
        confirm.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Iniciando viaje", Toast.LENGTH_SHORT).show()

            viaje.cost = (viaje.duration/60) * (viaje.distance/1000) * 0.5

            //Create trip finished dialog
            createCompletedTripDialog(data)

            //Open google maps gps
            val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1" +
                    "&origin=${viaje.origin.latitude},${viaje.origin.longitude}" +
                    "&destination=${viaje.destination.latitude},${viaje.destination.longitude}" +
                    "&travelmode=driving&dir_action=navigate")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun createCompletedTripDialog(data: Pair<String, Viaje>) {
        val viaje = data.second
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_trip_survey, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        builder.setOnDismissListener {
            fragmentManager!!.beginTransaction()
                .replace(R.id.mainContent, TripsPagerFragment())
                .commit()
        }
        val dialog = builder.show()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val total = dialogView.findViewById<TextView>(R.id.surveyTotal)
        total.text = viaje.cost.toString()
        val dateTime = dialogView.findViewById<TextView>(R.id.surveyDateTime)
        dateTime.text = viaje.dateTime.toDate().toString()
        val cliente = dialogView.findViewById<TextView>(R.id.name)
        cliente.text = viaje.userName

        val confirm = dialogView.findViewById<Button>(R.id.surveyConfirm)
        confirm.setOnClickListener {
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
            dialog.dismiss()
            tripService.updateCompletedTrip(data.first, ratingBar.rating)
            Toast.makeText(requireContext(), "Rating: ${ratingBar.rating}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createCancelTripDialog(data: Pair<String, Viaje>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cancelar viaje")
        builder.setMessage("¿Desea cancelar el viaje seleccionado?")

        builder.setPositiveButton("Si") { dialog, which ->
            Toast.makeText(
                requireContext(),
                "Cancelando viaje", Toast.LENGTH_SHORT
            ).show()

            MainScope().launch {
                tripService.cancelPendingTrip(data.first)
                updateData()
            }
        }

        builder.setNegativeButton("No") { dialog, which ->
            Toast.makeText(
                requireContext(),
                "Ok", Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
    }
}
