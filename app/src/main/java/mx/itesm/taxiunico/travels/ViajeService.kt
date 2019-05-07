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
package mx.itesm.taxiunico.travels

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.TripStatus
import mx.itesm.taxiunico.models.Viaje

class ViajeService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getTravelHistory(id: String): MutableList<Pair<String, Viaje>> {
        val res = db.collection(CODE_COLLECTION_KEY).whereEqualTo("userId", id).get().await()

        return res.documents.map { Pair(it.id, it.toObject(Viaje::class.java)!!) }.toMutableList()
    }

    fun addUserSurveyAnswer(userId: String, tripId: String, rating: Float) {
        db.collection("trips").document(tripId)
            .update(
                "userRating", rating,
                "pendingSurvey", false

            ).addOnCompleteListener{}
    }

    fun updateCompletedTrip(id: String, rating: Float, viaje: Viaje) {
        db.collection("trips").document(id)
            .update(
                "status", TripStatus.COMPLETED,
                "userRating", rating,
                "cost", viaje.cost,
                "payment", ".... 5248"
            ).addOnCompleteListener{}
    }

    suspend fun cancelPendingTrip(id: String) {
        db.collection("trips").document(id)
            .update(
                "status", TripStatus.CANCELED
            ).await()
    }

    companion object {
        const val CODE_COLLECTION_KEY = "trips"
    }
}

