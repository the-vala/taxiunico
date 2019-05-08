package mx.itesm.taxiunico.util

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import mx.itesm.taxiunico.models.Viaje
import java.text.SimpleDateFormat
import java.util.*


/**
 * Convierte un [LatLng] de Google Maps de Firestore a un [GeoPoint].
 */
fun LatLng.toGeoPoint(): GeoPoint {
    return GeoPoint(this.latitude, this.longitude)
}

fun String.toLocalDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd").parse(this)
}

/**
 * Convierte un [GeoPoint] de Firestore a un [LatLng] de Google Maps.
 */
fun GeoPoint.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

fun String.toSentenceCase(): String = this.toLowerCase().capitalize()

/**
 * Convierte un [DocumentSnapshot] en un [Pair] de su id y [T]
 */
inline fun <reified T> DocumentSnapshot.toIdPair(): Pair<String, T>? =
    if (this.exists()) Pair(this.id, this.toObject(T::class.java)!!) else null


/**
 * Convierte un [DocumentSnapshot] en una [List] de [Pair<String, T>] definidos.
 */
inline fun <reified T>List<DocumentSnapshot>.toIdPairList(): List<Pair<String, T>> =
    this.filter { it.exists() }
        .map { it.toIdPair<T>()!! }


/**
 * Calcula el cost de un viaje.
 */
fun Viaje.cost(): Double = (this.duration/60) * (this.distance/1000) * 0.5
