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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import mx.itesm.taxiunico.MainActivity
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.UserProfile
import mx.itesm.taxiunico.profile.UserService

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val userService = UserService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        signupBtn.setOnClickListener {
            createAccount(signupInputName.text.toString(),
                          signupInputEmail.text.toString(),
                          signupInputPhone.text.toString(),
                          signupInputPass.text.toString())
        }

        signupIniciaSesionBtn.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun createAccount(name: String, email: String, phone: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "Registrando", Toast.LENGTH_SHORT).show()
                    val id = auth.uid

                    val profile = UserProfile(signupInputName.text.toString(),
                        "",
                        "MX",
                        signupInputEmail.text.toString(),
                        signupInputPhone.text.toString())
                    userService.createProfile(id!!, profile){
                        Toast.makeText(this, "Usuario creado con exito", Toast.LENGTH_SHORT).show()
                    }
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.putExtra(USER, profile.email)
                    startActivity(mainIntent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val name = signupInputName.text.toString()
        if (TextUtils.isEmpty(name)) {
            signupInputName.error = "Required."
            valid = false
        } else {
            signupInputName.error = null
        }

        val email = signupInputEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            signupInputEmail.error = "Required."
            valid = false
        } else {
            signupInputEmail.error = null
        }

        val phone = signupInputPhone.text.toString()
        if (TextUtils.isEmpty(phone)) {
            signupInputPhone.error = "Required."
            valid = false
        } else {
            signupInputPhone.error = null
        }

        val password = signupInputPass.text.toString()
        if (TextUtils.isEmpty(password)) {
            signupInputPass.error = "Required."
            valid = false
        }
        else if (password.length < 6) {
            signupInputPass.error = "At least 6 characters."
            valid = false
        }
        else {
            signupInputPass.error = null
        }

        return valid
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val USER = "currentUser"
    }
}