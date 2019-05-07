package mx.itesm.taxiunico.travels

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.Viaje

class ViajeService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getTravelHistory(id: String): MutableList<Pair<String, Viaje>> {
        val res = db.collection(CODE_COLLECTION_KEY).whereEqualTo("userId", id).get().await()

        return res.documents.map { Pair(it.id, it.toObject(Viaje::class.java)!!) }.toMutableList()
    }

    fun addUserSurveyAnswer(userId: String, tripId: String, rating: Float) {
        db.collection("trips").document(tripId)
            .update(
                "driverRating", rating,
                "pendingSurvey", false

            ).addOnCompleteListener{}
    }

    fun updateCompletedTrip(id: String, rating: Float, viaje: Viaje) {
        db.collection("trips").document(id)
            .update(
                "completed", true,
                "userRating", rating,
                "cost", viaje.cost,
                "payment", ".... 5248"
            ).addOnCompleteListener{}
    }

    companion object {
        const val CODE_COLLECTION_KEY = "trips"
    }
}

