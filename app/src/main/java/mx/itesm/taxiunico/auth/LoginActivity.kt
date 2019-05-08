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
package mx.itesm.taxiunico.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import mx.itesm.taxiunico.R
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*

import mx.itesm.taxiunico.MainActivity
import mx.itesm.taxiunico.services.AuthService
import mx.itesm.taxiunico.services.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authService = AuthService(this)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            signIn(loginInputEmail.text.toString(), loginInputPass.text.toString())
        }

        loginRegistrateBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }

        loginContraseñaBtn.setOnClickListener {
            var valid = true
            val email = loginInputEmail.text.toString()
            if (TextUtils.isEmpty(email)) {
                loginInputEmail.error = "Required."
                valid = false
            } else {
                loginInputEmail.error = null
            }
            if (valid) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email enviado.")
                        }
                    }
                Toast.makeText(this,"Se te ha enviado un correo para restablecer tu contraseña.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Usa el email y el password para autenticar la cuenta.
     */
    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            when(authService.authenticate(email, password)) {
                is Result.Success ->
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                is Result.Failure ->
                    Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
    * Valida que se haya ingresado un email y password.
     * Confirma que el password sea mayor de 6 caracteres.
    */
    private fun validateForm(): Boolean {
        var valid = true

        val email = loginInputEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginInputEmail.error = "Required."
            valid = false
        } else {
            loginInputEmail.error = null
        }

        val password = loginInputPass.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginInputPass.error = "Required."
            valid = false
        }
        else if (password.length < 6) {
            loginInputPass.error = "At least 6 characters."
            valid = false
        }
        else {
            loginInputPass.error = null
        }

        return valid
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val USER = "currentUser"
    }
}
