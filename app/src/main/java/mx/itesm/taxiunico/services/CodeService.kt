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

import android.content.res.Resources
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.Codes


class CodeService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getTravelData(reservationCode: String): Result<Codes> {
        val res = db.collection(CODE_COLLECTION_KEY).document(reservationCode).get().await()

        if (!res.exists()) {
            return Result.Failure(Resources.NotFoundException("reservation code $reservationCode not found"))
        }

        val trip = res.toObject(Codes::class.java)!!
        return Result.Success(trip)
    }

    companion object {
        const val CODE_COLLECTION_KEY = "codes"
    }
}

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val result: T): Result<T>()
    data class Failure(val result: Throwable?): Result<Nothing>()
}
