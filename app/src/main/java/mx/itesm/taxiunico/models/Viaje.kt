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