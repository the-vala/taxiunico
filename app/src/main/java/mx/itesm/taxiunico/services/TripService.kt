package mx.itesm.taxiunico.services

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.FreshTrip

class TripService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addTrips(trips: List<FreshTrip>): Result<Unit> {
        trips.forEach {
            trip -> db.collection(CODE_COLLECTION_KEY).add(trip).await()
        }

        return Result.Success(Unit)
    }

    companion object {
        const val CODE_COLLECTION_KEY = "trips"
    }
}