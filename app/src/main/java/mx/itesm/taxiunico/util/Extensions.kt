package mx.itesm.taxiunico.util

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Regresa un GeoPoint de firebase con latitud y logitud
 * */
fun LatLng.toGeoPoint(): GeoPoint {
    return GeoPoint(this.latitude, this.longitude)
}

/**
 * @return objetivo SimpleDateFormat con el formato
 * yyyy-MM-dd definido
* */
fun String.toLocalDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd").parse(this)
}

/**
 * @return un LatLng con latitud y logitud
 * */
fun GeoPoint.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

/**
 * converite el string en lowercase y despues lo capitaliza.
 * */
fun String.toSentenceCase(): String = this.toLowerCase().capitalize()
