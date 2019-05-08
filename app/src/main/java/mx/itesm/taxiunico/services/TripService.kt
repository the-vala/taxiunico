package mx.itesm.taxiunico.services

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.flowViaChannel
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.models.FreshTrip
import mx.itesm.taxiunico.models.TripStatus
import mx.itesm.taxiunico.models.Viaje
import mx.itesm.taxiunico.util.toIdPairList

class TripService {
    /*** Referencia a la instancia de Firestore */
    private val db = FirebaseFirestore.getInstance()
    /*** Referencia a la collecion de viajes */
    private val collection = db.collection(TRIP_COLLECTION_KEY)

    /**
     * Añade una lista de [Viaje]
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
     * Trae los viajes para el usuario.
     */
    suspend fun getTravelHistory(id: String): MutableList<Pair<String, Viaje>> {
        val res = collection.whereEqualTo(Viaje::userId.name, id).get().await()

        return res.documents.toIdPairList<Viaje>().toMutableList()
    }

    /**
     * Emite los viajes disponibles para los conductores que estan en la ciudad [cityId],
     * cada vez que haya actualizaciones a esta lista.
     */
    @FlowPreview
    fun getRealTimeDriverHistory() = flowViaChannel<MutableList<Pair<String, Viaje>> > { channel ->
        collection
            .whereEqualTo(Viaje::status.name, TripStatus.PENDING)
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
     * Cancela el viaje [tripId].
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
