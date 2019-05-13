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

/**
 * Servicio que administra a los usuarios registrados del sistema
 */
class UserService {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection(USER_COLLECTION_KEY)

    /**
     * Función que recupera el perfil del usuario de la base de datos basado en el ID del usuario
     */
    suspend fun getProfile(userId: String): UserProfile? {
        val res = collection.document(userId).get().await()
        val user = res.toObject(UserProfile::class.java)
        return user
    }

    /**
     * Función que actualiza la información del usuario en la base de datos
     */
    suspend fun updateProfile(userId: String, userProfile: UserProfile) {
        db.collection(USER_COLLECTION_KEY).document(userId).set(userProfile).await()
    }


    fun updateScore(userId: String, score: Float) {
        db.runTransaction { transaction ->
            val user = transaction.get(collection.document(userId)).toObject(UserProfile::class.java)!!

            val newRating = ((user.surveyScore * user.tripCount) + score) / (user.tripCount + 1.0)

            // Update restaurant
            transaction.set(collection.document(userId), user.copy(
                surveyScore = newRating,
                tripCount = user.tripCount + 1
            ))
        }
    }

    companion object {
        const val USER_COLLECTION_KEY = "users"
    }
}


