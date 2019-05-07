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
package mx.itesm.taxiunico.services

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.UserProfile


class UserService {
    private val db = FirebaseFirestore.getInstance()


    suspend fun getProfile(userId: String): UserProfile? {
        val res = db.collection(USER_COLLECTION_KEY).document(userId).get().await()
        val user = res.toObject(UserProfile::class.java)
        return user
    }

    suspend fun updateProfile(userId: String, userProfile: UserProfile) {
        db.collection(USER_COLLECTION_KEY).document(userId).set(userProfile).await()
    }

    companion object {
        const val USER_COLLECTION_KEY = "users"
    }
}


