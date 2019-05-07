package mx.itesm.taxiunico.Database

import androidx.room.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

@Entity(tableName = "Trip")
data class Trip (@ColumnInfo(name = "userId") val userId:String,
                 @ColumnInfo(name = "codeId") val codeId:String,
                 @ColumnInfo(name = "dateTime") val dateTime:Timestamp,
                 @ColumnInfo(name = "origin") val origin:GeoPoint,
                 @ColumnInfo(name = "destination") val destination:GeoPoint,
                 @ColumnInfo(name = "driverName") val driverName:String,
                 @ColumnInfo(name = "vehicle") val vehicle:String,
                 @ColumnInfo(name = "distance") val distance:Double,
                 @ColumnInfo(name = "cost") val cost:Double,
                 @ColumnInfo(name = "payment") val payment:String,
                 @ColumnInfo(name = "completed") val completed:Boolean){

    companion object {
    }
}
