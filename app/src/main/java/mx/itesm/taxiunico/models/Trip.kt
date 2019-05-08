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
package mx.itesm.taxiunico.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.util.*

/**
 * Clase para definir el estatus del viaje
 */
enum class TripStatus {
    PENDING,
    COMPLETED,
    CANCELED
}

/**
 * Modelo para definir viaje recién solicitado y sin información completa
 */
data class FreshTrip(
    val userId: String,
    var dateTime: Date,
    var origin: GeoPoint,
    var destination: GeoPoint,
    var status: TripStatus = TripStatus.PENDING
)

/**
 * Modelo para definir un viaje iniciado o terminado con información mucho mas robusta
 */
data class Viaje(
    var userId: String = "",
    var userName: String = "",
    var driverId: String = "",
    var driverName: String = "",
    var userRating: Int = 5,
    var codeId: String = "",
    var dateTime: Timestamp = Timestamp.now(),
    var origin: GeoPoint = GeoPoint(0.0,0.0),
    var destination: GeoPoint = GeoPoint(0.0,0.0),
    var driverRating: Int = 5,
    var vehicle: String = "",
    var distance: Double = 0.0,
    var duration: Double = 0.0,
    var cost: Double = 0.0,
    var payment: String = "",
    var status: TripStatus = TripStatus.PENDING,
    var pendingSurvey: Boolean = false,
    var imageURL: String = ""
)