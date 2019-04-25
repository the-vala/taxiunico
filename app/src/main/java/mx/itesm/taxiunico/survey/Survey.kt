package mx.itesm.taxiunico.survey

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey (
    val score:Int,
    val date: String
): Parcelable {
    companion object {
        fun loadEncuestas(): MutableList<Survey> {

            val listSurveys: MutableList<Survey> = mutableListOf()

            return listSurveys
        }
    }
}
