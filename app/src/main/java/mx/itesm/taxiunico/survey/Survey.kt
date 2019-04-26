package mx.itesm.taxiunico.survey

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey (
    val score:Int = 0,
    val date: String = ""
): Parcelable
