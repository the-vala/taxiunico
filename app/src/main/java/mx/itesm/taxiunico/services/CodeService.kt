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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.Codes

/**
 * Servicio para adminsitrar códigos de reservación
 */
class CodeService {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection(CODE_COLLECTION_KEY)

    /**
     * Función que regresa la informacion relacionada al código de reservacion como origen y destino
     */
    suspend fun getTravelData(reservationCode: String): Result<Codes> {
        val res = db.collection(CODE_COLLECTION_KEY).document(reservationCode).get().await()

        if (!res.exists()) {
            Result.Failure(Resources.NotFoundException("reservation code $reservationCode not found"))
        }

        Result.Success(res.toObject(Codes::class.java)!!)
    } catch (err: Throwable) {
        Result.Failure(err)
    }

    companion object {
        const val CODE_COLLECTION_KEY = "codes"
    }
}