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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.*
import mx.itesm.taxiunico.MainActivity
import mx.itesm.taxiunico.Network.ConnectionViewModel
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.UserProfile
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.prefs.UserPrefs
import mx.itesm.taxiunico.services.AuthService
import mx.itesm.taxiunico.services.UserService
import kotlin.coroutines.CoroutineContext

/**
 * Fragmento del perfil del usuario
 */
class UserProfileFragment : Fragment() {
    private val userService = UserService()
    private lateinit var authService: AuthService
    private val auth = FirebaseAuth.getInstance()

    private lateinit var userProfile: UserProfile
    private lateinit var model: ConnectionViewModel
    private var job = Job()

    val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(requireActivity()).get(ConnectionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_user_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authService = AuthService(requireContext())

        userProfile = authService.getUserProfile()

        pullNewUserProfileChanges()

        render(userProfile)

        button.setOnClickListener {
            saveProfile()
        }

        logout.setOnClickListener {
            signOut()
        }
    }

    /**
     * Refresca los datos de la vista
     */
    private fun render(userProfile: UserProfile) {
        title.text =  when(userProfile.userType) {
            UserType.DRIVER -> "Detalles del Conductor"
            UserType.TRAVELER -> "Detalles del Usuario"
        }

        nameInput.setText(userProfile.name)
        emailInput.setText(userProfile.email)
        phoneInput.setText(userProfile.phone)
        score.setText(getString(R.string.calificacion, userProfile.surveyScore))
    }

    /**
     * Función que guarda los nuevos datos del usuario en la base de datos y muestra un mensaje de confirmación
     */
    private fun saveProfile() {
        if (validateForm() && model.getConnectionState().value!!) {
            Toast.makeText(requireContext(), "Guardando", Toast.LENGTH_SHORT).show()

            val newProfile = UserProfile(
                name = nameInput.text.toString(),
                email = emailInput.text.toString(),
                phone = phoneInput.text.toString()
            )

            UserPrefs(requireContext()).userProfile = newProfile

            CoroutineScope(coroutineContext).launch {
                userService.updateProfile(authService.getUserUid()!!, newProfile)
                Toast.makeText(requireContext(), "Perfil Guardado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No hay conexión", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }


    /**
     * Baja los cambios mas recientes del usuario y los guarda en userprefs.
     */
    private fun pullNewUserProfileChanges() {
        CoroutineScope(coroutineContext).launch {
            userService.getProfile(authService.getUserUid()!!)?.let {
                UserPrefs(requireContext()).userProfile = it
                render(it)
            }
        }
    }

    /**
     * Revisa si los datos introducidos en la forma son validos
     */
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

    /**
     * Revisa si un email tiene un formato válido
     */
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.toRegex().matches(email)
    }


    /**
     * Revisa si un telefono tiene un formato válido
     */
    private fun isPhoneValid(phone: String): Boolean {
        return Patterns.PHONE.toRegex().matches(phone)
    }

    /**
     * Función para hacer sign out. Carga la visa de login y restablece el auth de la aplicación.
     */
    private fun signOut() {
        if (model.getConnectionState().value!!) {
            auth.signOut()
            UserPrefs(requireContext()).clear()
            val mainIntent = Intent(context, MainActivity::class.java)
            startActivity(mainIntent)
        } else {
            Toast.makeText(context, "No hay conexión", Toast.LENGTH_SHORT).show()
        }
    }

}
