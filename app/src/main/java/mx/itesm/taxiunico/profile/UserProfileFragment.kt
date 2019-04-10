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
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import mx.itesm.taxiunico.MainActivity
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.UserProfile

class UserProfileFragment : Fragment() {

    private val userService = UserService()

    private val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_user_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        render()

        button.setOnClickListener {
            saveProfile()
        }

        logout.setOnClickListener {
            signOut()
        }
    }

    private fun render() {
        userService.getProfile(auth.uid!!) {
            it?.let {
                if (it.name != null)  {
                    nameInput.setText(it.name)
                    lastnameInput.setText(it.lastname)
                    emailInput.setText(it.email)
                    phoneInput.setText(it.phone)
                }
            }
        }
    }

    private fun saveProfile() {
        Toast.makeText(requireContext(), "Guardando", Toast.LENGTH_SHORT).show()
        userService.updateProfile(auth.uid!!, UserProfile(
            name = nameInput.text.toString(),
            lastname = lastnameInput.text.toString(),
            email = emailInput.text.toString(),
            phone = phoneInput.text.toString()
        )
        ) {
            Toast.makeText(requireContext(), "Perfil Guardado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        auth.signOut()
        val mainIntent = Intent(context, MainActivity::class.java)
        startActivity(mainIntent)
    }
}