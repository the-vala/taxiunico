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
package mx.itesm.taxiunico

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.auth.LoginActivity
import mx.itesm.taxiunico.billing.PaymentFormsFragment
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.profile.UserProfileFragment
import mx.itesm.taxiunico.travels.TripsPagerFragment
import mx.itesm.taxiunico.trips.CheckTripCodeFragment
import android.os.PersistableBundle
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.models.Viaje
import mx.itesm.taxiunico.services.TripService
import mx.itesm.taxiunico.travels.ViajeService
import mx.itesm.taxiunico.util.ConnectivityReceiver
import android.R.attr.gravity
import android.widget.FrameLayout



@SuppressLint("Registered")
class MainActivity : AppCompatActivity(),
    ConnectivityReceiver.ConnectivityListener {

    private lateinit var authService: AuthService
    private var saveState: Int = 0
    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        setContentView(R.layout.activity_main)

        authService = AuthService(this)

        if (authService.isUserAuthenticated()) {
            if (savedInstanceState != null) {
                nav.setSelectedItemId(saveState)
            } else {
                openDefaultFragment()
            }

            checkPendingSurveys()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        when(authService.getUserType()) {
            UserType.TRAVELER -> nav.inflateMenu(R.menu.traveler_menu)
            UserType.DRIVER -> nav.inflateMenu(R.menu.driver_menu)
        }

        nav.setOnNavigationItemSelectedListener { navigate(it) }
    }

    private fun checkPendingSurveys() = MainScope().launch {
        val pendingSurveyTrip = TripService().getPendingSurveyTrip(this@MainActivity)

        pendingSurveyTrip?.let {
            showUserSurvey(it.first, it.second)
        }
    }

    private fun showUserSurvey(tripId: String, viaje: Viaje) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_trip_survey, null)
        val builder = AlertDialog.Builder(this).setView(dialogView)
        builder.setOnDismissListener {

        }
        val dialog = builder.show()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val total = dialogView.findViewById<TextView>(R.id.surveyTotal)
        total.text = viaje.cost.toString()

        // TODO(terminar de poner info de este layout)
        dialogView.findViewById<Button>(R.id.surveyConfirm).setOnClickListener {
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
            dialog.dismiss()
            ViajeService().addUserSurveyAnswer(
                userId = authService.getUserUid()!!,
                tripId = tripId,
                rating = ratingBar.rating)

            Toast.makeText(this, "Rating: ${ratingBar.rating}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun openDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContent, UserProfileFragment())
            .commit()
    }

    private fun navigate(menuItem: MenuItem): Boolean {
        val targetFragment =
        when(menuItem.itemId) {
            R.id.profileMenu -> UserProfileFragment()
            R.id.paymentMenu -> PaymentFormsFragment()
            R.id.newTripMenu -> CheckTripCodeFragment()
            R.id.travelerTravelMenu -> TripsPagerFragment()
            R.id.travelMenu -> TripsPagerFragment()
            else -> throw Throwable("Invalid menu option selected")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContent, targetFragment)
            .commit()

        return true
    }

    @SuppressLint("WrongConstant")
    private fun showConnectionMessage(isConnected: Boolean) {

        if (!isConnected) {
            val message = "No hay conexion."
            mSnackBar = Snackbar.make(findViewById(R.id.mainContent), message, Snackbar.LENGTH_LONG)
            mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
            val view = mSnackBar!!.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            view.layoutParams = params
            mSnackBar?.show()
        } else {
            mSnackBar?.dismiss()
        }
    }

    override fun onNetworkChanged(isConnected: Boolean) {
        showConnectionMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityListener = this
        nav.setSelectedItemId(saveState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        saveState = nav.getSelectedItemId()
    }

}
