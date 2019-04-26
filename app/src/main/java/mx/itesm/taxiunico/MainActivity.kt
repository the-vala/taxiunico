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

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.auth.LoginActivity
import mx.itesm.taxiunico.billing.PaymentFormsFragment
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.profile.UserProfileFragment
import mx.itesm.taxiunico.survey.SurveyDialogFragment

class MainActivity : AppCompatActivity() {
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        authService = AuthService(this)


        if (authService.isUserAuthenticated()) {
            openDefaultFragment()

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }


        nav.menu.clear()
        when(authService.getUserType()) {
            UserType.TRAVELER -> nav.inflateMenu(R.menu.traveler_menu)
            UserType.DRIVER -> nav.inflateMenu(R.menu.driver_menu)
        }

        nav.setOnNavigationItemSelectedListener { navigate(it) }
        //TODO Eliminar esto de main
        //Eliminar esto lol
        //val surveyInstance = SurveyDialogFragment()
        //surveyInstance.show(supportFragmentManager, "survey")
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContent, SurveyDialogFragment())
            .commit()
        //hasta aqui
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
            else -> throw Error()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContent, targetFragment)
            .commit()

        return true
    }
}
