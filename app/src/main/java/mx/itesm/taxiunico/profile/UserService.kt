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

import com.google.firebase.firestore.FirebaseFirestore
import mx.itesm.taxiunico.models.UserProfile


class UserService {
    private val db = FirebaseFirestore.getInstance()

    fun getProfile(userId: String, onComplete: (UserProfile?) -> Unit) {
        db.collection(USER_COLLECTION_KEY).document(userId).get().addOnCompleteListener {
            val res = it.result.toObject(UserProfile::class.java)
            onComplete(res)
        }
    }

    fun updateProfile(userId: String, userProfile: UserProfile, onSuccess: () -> Unit) {
        db.collection(USER_COLLECTION_KEY).document(userId).set(userProfile).addOnSuccessListener {
            onSuccess()
        }
    }

    fun createProfile(userId: String, userProfile: UserProfile, onSuccess: () -> Unit) {
        val user: HashMap<String, Any> = hashMapOf()
        user.put("country", userProfile.country)
        user.put("email", userProfile.email)
        user.put("lastname", userProfile.lastname)
        user.put("name", userProfile.name)
        user.put("phone", userProfile.phone)
        db.collection(USER_COLLECTION_KEY).document(userId).set(user).addOnSuccessListener {
            onSuccess()
        }
    }

    companion object {
        const val USER_COLLECTION_KEY = "users"
    }
}


