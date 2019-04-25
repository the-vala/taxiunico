package mx.itesm.taxiunico.trips

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.UserProfile


data class Station(
    val cityId: String = "",
    val city: String = "",
    val cord: GeoPoint? = null
)

class BusStationService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getStations(): List<Station> {
        val res = db.collection(BUS_STATION_COLLECTION_KEY).get().await()
        return res.documents
            .filter { it.exists() }
            .map { it.toObject(Station::class.java)!!.copy(cityId = it.id) }
    }

    companion object {
        const val BUS_STATION_COLLECTION_KEY = "stations"
    }
}

