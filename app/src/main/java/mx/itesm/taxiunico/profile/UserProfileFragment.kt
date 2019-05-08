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
package mx.itesm.taxiunico.profile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.MainActivity
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.models.UserProfile
import mx.itesm.taxiunico.prefs.UserPrefs
import mx.itesm.taxiunico.services.UserService

class UserProfileFragment : Fragment() {
    private val userService = UserService()
    private lateinit var authService: AuthService
    private val auth = FirebaseAuth.getInstance()

    private lateinit var userProfile: UserProfile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_user_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authService = AuthService(requireContext())
        userProfile = authService.getUserProfile()

        render(userProfile)

        button.setOnClickListener {
            saveProfile()
        }

        logout.setOnClickListener {
            signOut()
        }
    }

    private fun render(userProfile: UserProfile) {
        nameInput.setText(userProfile.name)
        emailInput.setText(userProfile.email)
        phoneInput.setText(userProfile.phone)
    }

    private fun saveProfile() {
        if (validateForm()) {
            Toast.makeText(requireContext(), "Guardando", Toast.LENGTH_SHORT).show()

            MainScope().launch {
                userService.updateProfile(
                    authService.getUserUid()!!, UserProfile(
                        name = nameInput.text.toString(),
                        email = emailInput.text.toString(),
                        phone = phoneInput.text.toString()
                    )
                )

                Toast.makeText(requireContext(), "Perfil Guardado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val name = nameInput.text.toString()
        if (TextUtils.isEmpty(name)) {
            nameInput.error = "Requerido"
            valid = false
        } else {
            nameInput.error = null
        }

        val email = emailInput.text.toString()
        if (isEmailValid(email)) {
            emailInput.error = null
        } else {
            emailInput.error = "Inválido"
            valid = false
        }

        val phone = phoneInput.text.toString()
        if (isPhoneValid(phone)) {
            phoneInput.error = null
        } else {
            phoneInput.error = "Inválido"
            valid = false
        }

        return valid
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.toRegex().matches(email)
    }
    private fun isPhoneValid(phone: String): Boolean {
        return Patterns.PHONE.toRegex().matches(phone)
    }

    private fun signOut() {
        auth.signOut()
        UserPrefs(requireContext()).clear()
        val mainIntent = Intent(context, MainActivity::class.java)
        startActivity(mainIntent)
    }

}
