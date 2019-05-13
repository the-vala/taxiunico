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
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import mx.itesm.taxiunico.services.AuthService
import mx.itesm.taxiunico.auth.LoginActivity
import mx.itesm.taxiunico.billing.PaymentFormsFragment
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.profile.UserProfileFragment
import mx.itesm.taxiunico.travels.TripsPagerFragment
import mx.itesm.taxiunico.trips.CheckTripCodeFragment
import android.os.PersistableBundle
import mx.itesm.taxiunico.models.Viaje
import mx.itesm.taxiunico.services.TripService
import mx.itesm.taxiunico.util.ConnectivityReceiver
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import mx.itesm.taxiunico.auth.BaseActivity
import mx.itesm.taxiunico.trips.InProgressTripFragment
import mx.itesm.taxiunico.trips.UserSurveyDialog
import kotlin.coroutines.CoroutineContext


@SuppressLint("Registered")
class MainActivity : BaseActivity() {
    private lateinit var authService: AuthService
    private var saveState: Int = 0

    private var job = Job()
    val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authService = AuthService(this)

        if (authService.isUserAuthenticated()) {
            if (savedInstanceState != null) {
                nav.selectedItemId = saveState
            } else {
                openDefaultFragment()
            }

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        when(authService.getUserType()) {
            UserType.TRAVELER -> {
                nav.inflateMenu(R.menu.traveler_menu)
                nav.setBackgroundColor(ContextCompat.getColor(this, R.color.taxiBlue))
                checkPendingSurveys()
                checkInProgressTrip()
            }
            UserType.DRIVER -> {
                nav.inflateMenu(R.menu.driver_menu)
                nav.setBackgroundColor(ContextCompat.getColor(this, R.color.taxiBlack))
                nav.itemTextColor = ContextCompat.getColorStateList(this, R.color.black_menu_color)
                nav.itemIconTintList = (ContextCompat.getColorStateList(this, R.color.black_menu_color))

            }
        }

        ConnectivityReceiver.connectivityListener = this
        nav.setOnNavigationItemSelectedListener { navigate(it) }

        coroutineContext[Job]?.cancel()
    }

    /**
     * Función que revisa si existen viajes con encuestas pendientes. Si existe, le pide al usuario contestar
     * la encuesta de la mas reciente al abrir la app
     */
    @FlowPreview
    private fun checkPendingSurveys() = CoroutineScope(coroutineContext).launch {
        TripService().getPendingSurveyTrip(this@MainActivity).takeWhile {
            authService.getUserType() == UserType.TRAVELER
        }.collect {
            showUserSurvey(it.first, it.second)
        }
    }

    @FlowPreview
    private fun checkInProgressTrip() = CoroutineScope(coroutineContext).launch {
        TripService().getInProgressTrip(this@MainActivity).collect {
            showCurrentTrip(it.first, it.second)
        }
    }

    /**
     * Función que recibe el id del viaje sin encuesta mas reciente y muestra dicha encuesta al usuario
     */
    private fun showUserSurvey(tripId: String, viaje: Viaje) {
        supportFragmentManager.popBackStack()

        UserSurveyDialog(this@MainActivity).show(tripId, viaje)
    }

    private fun showCurrentTrip(tripId: String, viaje: Viaje) {
        val tag = "CURRENT_TRIP"

        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .replace(
                android.R.id.content,
                InProgressTripFragment.newInstance(
                    destinationAddress = viaje.endAddress,
                    originAddress = viaje.startAddress,
                    mapUrl = viaje.imageURL,
                    driverName = viaje.driverName,
                    distance = viaje.distance,
                    duration = viaje.duration
                )
            )
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    /**
     * Función que abre la vista default al iniciar la aplicación
     */
    private fun openDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContent, UserProfileFragment())
            .commitAllowingStateLoss()
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
            .commitAllowingStateLoss()

        return true
    }

    override fun onPause() {
        super.onPause()
        coroutineContext[Job]?.cancel()
    }

    /**
     * Función que revisa el estado de la conexión y carga la opción del menu seleccionada al resumir la actividad
     */
    override fun onResume() {
        super.onResume()
        nav.selectedItemId = saveState
    }

    /**
     * Función que guarda la opción seleccionada del menu en el companión object al interrumpir la actividad
     */
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        saveState = nav.selectedItemId
    }
}
