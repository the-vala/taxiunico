package mx.itesm.taxiunico

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Encuesta (
    val id:Int,
    val calificacion:Int,
    val fecha: String
):Parcelable {
    companion object {
        @SuppressLint("ResourceType")
        fun loadEncuestas(): MutableList<Encuesta> {

            val listEncuestas: MutableList<Encuesta> = mutableListOf()

            //Datos de prueba
            listEncuestas.add(Encuesta(12324, 5, "12/01/2019"))
            listEncuestas.add(Encuesta(12325, 4, "22/02/2019"))
            listEncuestas.add(Encuesta(12326, 4, "05/03/2019"))

            return listEncuestas
        }
    }

}
