package mx.itesm.taxiunico.services

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.models.FreshTrip
import mx.itesm.taxiunico.models.TripStatus
import mx.itesm.taxiunico.models.Viaje

/**
 * Servicio de viajes que inserta y recuepera viajes de la base de datos
 */
class TripService {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection(TRIP_COLLECTION_KEY)

    /**
     * Funcion que inserta los viajes nuevos en la base de datos de firebase
     */
    suspend fun addTrips(trips: List<FreshTrip>): Result<Unit> {
        trips.forEach {
            trip -> collection.add(trip).await()
        }

        return Result.Success(Unit)
    }

    /**
     * Funcion que regresa la lista de viajes con encuesta pendiente
     */
    suspend fun getPendingSurveyTrip(context: Context): Pair<String, Viaje>? {
        val uid = AuthService(context).getUserUid()
        val res = collection
            .whereEqualTo("userId", uid)
            .whereEqualTo("pendingSurvey", true)
            .get().await()

        return res.map { Pair(it.id, it.toObject(Viaje::class.java)) }.firstOrNull()
    }

    /**
     * Funcion que regresa la lista de viajes del usuario
     */
    suspend fun getTravelHistory(id: String): MutableList<Pair<String, Viaje>> {
        val res = collection.whereEqualTo("userId", id).get().await()

        return res.documents.map { Pair(it.id, it.toObject(Viaje::class.java)!!) }.toMutableList()
    }

    /**
     * Funcion que inserta las encuestas hechas por el usuario en la tabla de viajes de firebase
     */
    fun addUserSurveyAnswer(userId: String, tripId: String, rating: Float) {
        collection.document(tripId)
            .update(
                "driverRating", rating,
                "pendingSurvey", false

            ).addOnCompleteListener{}
    }

    /**
     * Funcion que actualiza el estado de un viaje una vez que ha sido terminado
     */
    fun updateCompletedTrip(id: String, rating: Float) {
        collection.document(id)
            .update(
                "status", TripStatus.COMPLETED,
                "userRating", rating,
                "payment", ".... 5248",
                "pendingSurvey", true
            ).addOnCompleteListener{}
    }

    /**
     * Funcion que actualiza el estado de un viaje que ha sido cancelado
     */
    suspend fun cancelPendingTrip(id: String) {
        collection.document(id).update(
            "status", TripStatus.CANCELED
        ).await()
    }

    companion object {
        const val TRIP_COLLECTION_KEY = "trips"
    }
}

/**
 * Sealed class para determinar si el fetch de un viaje fue correcto o no
 */
sealed class Result<out T: Any> {
    data class Success<out T: Any>(val result: T): Result<T>()
    data class Failure(val result: Throwable?): Result<Nothing>()
}
