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

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.flowViaChannel
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.services.AuthService
import mx.itesm.taxiunico.models.FreshTrip
import mx.itesm.taxiunico.models.TripStatus
import mx.itesm.taxiunico.models.Viaje
import mx.itesm.taxiunico.util.toIdPairList

/**
 * Servicio de viajes que inserta y recuepera viajes de la base de datos
 */
class TripService {
    /*** Referencia a la instancia de Firestore */
    private val db = FirebaseFirestore.getInstance()
    /*** Referencia a la collecion de viajes */
    private val collection = db.collection(TRIP_COLLECTION_KEY)

    /**
     * Función que inserta una lista de [Viaje] nuevos en la base de datos de firebase
     */
    suspend fun addTrips(trips: List<FreshTrip>): Result<Unit> {
        trips.forEach {
            trip -> collection.add(trip).await()
        }

        return Result.Success(Unit)
    }

    /**
     * Emite un viaje que tenga una encuesta pendiente.
     */
    @FlowPreview
    fun getPendingSurveyTrip(context: Context) = flowViaChannel<Pair<String, Viaje>> { channel ->
        val uid = AuthService(context).getUserUid()
        collection
            .whereEqualTo(Viaje::userId.name, uid)
            .whereEqualTo(Viaje::pendingSurvey.name, true)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot!!.documents.toIdPairList<Viaje>().firstOrNull()?.let {
                    channel.sendBlocking(it)
                }
            }
    }

    /**
     * Función que regresa la lista de viajes del usuario
     */
    @FlowPreview
    fun getRealTimeCompletedHistory(id: String) = flowViaChannel<MutableList<Pair<String, Viaje>> > { channel ->
        collection
            .whereEqualTo(Viaje::userId.name, id)
            .whereEqualTo(Viaje::status.name, TripStatus.COMPLETED)
            .orderBy(Viaje::dateTime.name)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.documents?.let { docs ->
                    channel.sendBlocking(
                        docs.toIdPairList<Viaje>().toMutableList()
                    )
                }
            }
    }

    /**
     * Trae los viajes para el usuario.
     */
    @FlowPreview
    fun getRealTimeTravelerPendingHistory(id: String) = flowViaChannel<MutableList<Pair<String, Viaje>> > { channel ->
        collection
            .whereEqualTo(Viaje::userId.name, id)
            .whereEqualTo(Viaje::status.name, TripStatus.PENDING)
            .orderBy(Viaje::dateTime.name)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.documents?.let { docs ->
                    channel.sendBlocking(
                        docs.toIdPairList<Viaje>().toMutableList()
                    )
                }
            }
    }

    /**
     * Emite los viajes disponibles para los conductores que estan en la ciudad [cityId],
     * cada vez que haya actualizaciones a esta lista.
     */
    @FlowPreview
    fun getRealTimeDriverHistory(cityHub: String) = flowViaChannel<MutableList<Pair<String, Viaje>> > { channel ->
        collection
            .whereEqualTo(Viaje::status.name, TripStatus.PENDING)
            .whereEqualTo(Viaje::relatedCityId.name, cityHub)
            .orderBy(Viaje::dateTime.name)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.documents?.let { docs ->
                    channel.sendBlocking(
                        docs.toIdPairList<Viaje>().toMutableList()
                    )
                }
            }
        }

    /**
     * Asigna la calificacion que da el pasajero al conductor del viaje [tripId].
     */
    fun addUserSurveyAnswer(tripId: String, rating: Float) {
        collection.document(tripId)
            .update(
                Viaje::driverRating.name, rating,
                Viaje::pendingSurvey.name, false

            )
    }

    /**
     * Marca el viaje [tripId] como completado y le asigna una calificacion al pasajero..
     */
    fun updateCompletedTrip(tripId: String, rating: Float) {
        collection.document(tripId)
            .update(
                Viaje::completionDateTime.name, Timestamp.now(),
                Viaje::status.name, TripStatus.COMPLETED,
                Viaje::pendingSurvey.name,  true,
                Viaje::userRating.name, rating,
                Viaje::payment.name, ".... 5248"
            )
    }

    /**
     * Función que actualiza el estado de un viaje que ha sido cancelado
     */
    suspend fun cancelPendingTrip(tripId: String) {
          collection.document(tripId).update(
              Viaje::status.name, TripStatus.CANCELED
          ).await()
      }

    /**
     * Comienza el viaje [tripId] and se lo asigna al conductor [driverId].
     */
    fun startTrip(driverId: String?, tripId: String) {
        collection.document(tripId)
            .update(
                Viaje::startDateTime.name, Timestamp.now(),
                Viaje::driverId.name, driverId,
                Viaje::status.name, TripStatus.IN_PROGRESS)
    }

    companion object {
        /**
         * Llave de la coleccion de viajes en Firestore
         */
        const val TRIP_COLLECTION_KEY = "trips"
    }
}