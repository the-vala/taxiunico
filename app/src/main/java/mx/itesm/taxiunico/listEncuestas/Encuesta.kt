package mx.itesm.taxiunico.listEncuestas

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Encuesta (
    val id:Int,
    val calificacion:Int,
    val fecha: String
): Parcelable {
    companion object {
        fun loadEncuestas(): MutableList<Encuesta> {

            val listEncuestas: MutableList<Encuesta> = mutableListOf()

            return listEncuestas
        }
    }
}
