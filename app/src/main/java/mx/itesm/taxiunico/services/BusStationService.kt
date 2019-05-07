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
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await


data class Station(
    val cityId: String = "",
    val city: String = "",
    val cord: GeoPoint? = null
)

class BusStationService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getStations(): List<Station> {
        val res = db.collection(BUS_STATION_COLLECTION_KEY).get().await()
        return res.documents
            .filter { it.exists() }
            .map { it.toObject(Station::class.java)!!.copy(cityId = it.id) }
    }

    companion object {
        const val BUS_STATION_COLLECTION_KEY = "stations"
    }
}

