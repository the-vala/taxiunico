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

data class Viaje(
    var userId: String = "",
    var codeId: String = "",
    var dateTime: Timestamp = Timestamp.now(),
    var origin: GeoPoint = GeoPoint(0.0,0.0),
    var destination: GeoPoint = GeoPoint(0.0,0.0),
    var driverName: String = "",
    var vehicle: String = "",
    var distance: Double = 0.0,
    var cost: Double = 0.0,
    var payment: String = "",
    var completed: Boolean = false,
    var imageUrl: String = ""
)