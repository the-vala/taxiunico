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
import mx.itesm.taxiunico.survey.SurveyListFragment
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.getSystemService
import com.google.android.material.snackbar.Snackbar
import mx.itesm.taxiunico.util.ConnectivityReceiver
import java.net.ConnectException

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
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        when(authService.getUserType()) {
            UserType.TRAVELER -> nav.inflateMenu(R.menu.traveler_menu)
            UserType.DRIVER -> nav.inflateMenu(R.menu.driver_menu)
        }

        nav.setOnNavigationItemSelectedListener { navigate(it) }
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
