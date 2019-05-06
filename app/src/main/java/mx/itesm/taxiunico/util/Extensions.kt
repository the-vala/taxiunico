package mx.itesm.taxiunico.util

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.util.*


fun LatLng.toGeoPoint(): GeoPoint {
    return GeoPoint(this.latitude, this.longitude)
}

fun String.toLocalDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd").parse(this)
}

fun GeoPoint.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

fun String.toSentenceCase(): String = this.toLowerCase().capitalize()
