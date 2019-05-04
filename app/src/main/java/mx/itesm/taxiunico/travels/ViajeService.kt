package mx.itesm.taxiunico.travels

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.Viaje

class ViajeService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getTravelHistory(id: String): MutableList<Viaje> {
        val res = db.collection(CODE_COLLECTION_KEY).whereEqualTo("userId", id).get().await()

        val trips = res.toObjects(Viaje::class.java)
        return trips
    }

    companion object {
        const val CODE_COLLECTION_KEY = "trips"
    }
}

