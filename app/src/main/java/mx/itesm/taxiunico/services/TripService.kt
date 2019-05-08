package mx.itesm.taxiunico.services

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.models.FreshTrip
import mx.itesm.taxiunico.models.TripStatus
import mx.itesm.taxiunico.models.Viaje

class TripService {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection(TRIP_COLLECTION_KEY)

    suspend fun addTrips(trips: List<FreshTrip>): Result<Unit> {
        trips.forEach {
            trip -> collection.add(trip).await()
        }

        return Result.Success(Unit)
    }

    suspend fun getPendingSurveyTrip(context: Context): Pair<String, Viaje>? {
        val uid = AuthService(context).getUserUid()
        val res = collection
            .whereEqualTo("userId", uid)
            .whereEqualTo("pendingSurvey", true)
            .get().await()

        return res.map { Pair(it.id, it.toObject(Viaje::class.java)) }.firstOrNull()
    }

    suspend fun getTravelHistory(id: String): MutableList<Pair<String, Viaje>> {
        val res = collection.whereEqualTo("userId", id).get().await()

        return res.documents.map { Pair(it.id, it.toObject(Viaje::class.java)!!) }.toMutableList()
    }

    fun addUserSurveyAnswer(userId: String, tripId: String, rating: Float) {
        collection.document(tripId)
            .update(
                "driverRating", rating,
                "pendingSurvey", false

            ).addOnCompleteListener{}
    }

    fun updateCompletedTrip(id: String, rating: Float) {
        collection.document(id)
            .update(
                "status", TripStatus.COMPLETED,
                "userRating", rating,
                "payment", ".... 5248",
                "pendingSurvey", true
            ).addOnCompleteListener{}
    }

    suspend fun cancelPendingTrip(id: String) {
        collection.document(id).update(
            "status", TripStatus.CANCELED
        ).await()
    }

    companion object {
        const val TRIP_COLLECTION_KEY = "trips"
    }
}

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val result: T): Result<T>()
    data class Failure(val result: Throwable?): Result<Nothing>()
}
