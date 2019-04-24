package mx.itesm.taxiunico.models

import com.google.firebase.firestore.GeoPoint

data class Viaje(
    val destination: GeoPoint,
    val origin: GeoPoint,
    val driverName: String,
    val distance: Float,
    val completed: Boolean,
    val imageUrl: String
)