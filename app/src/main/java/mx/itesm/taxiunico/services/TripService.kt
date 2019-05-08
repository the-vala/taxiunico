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

    suspend fun addTrips(trips: List<FreshTrip>): Result<Unit> {
        trips.forEach {
            trip -> db.collection(CODE_COLLECTION_KEY).add(trip).await()
        }

        return Result.Success(Unit)
    }

    suspend fun getPendingSurveyTrip(context: Context): Pair<String, Viaje>? {
        val uid = AuthService(context).getUserUid()
        val res = db.collection(CODE_COLLECTION_KEY)
            .whereEqualTo("userId", uid)
            .whereEqualTo("pendingSurvey", true)
            .get().await()

        return res.map { Pair(it.id, it.toObject(Viaje::class.java)) }.firstOrNull()
    }

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
                "payment", ".... 5248",
                "pendingSurvey", true
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