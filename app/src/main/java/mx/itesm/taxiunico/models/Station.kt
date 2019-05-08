package mx.itesm.taxiunico.models

import com.google.firebase.firestore.GeoPoint

data class Station(
    val cityId: String = "",
    val city: String = "",
    val cord: GeoPoint? = null
)