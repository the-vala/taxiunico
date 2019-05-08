/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.itesm.taxiunico.Database

import androidx.room.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

//@Entity, indica el nombre del la entidad (tabla) en la base de datos
//@ColumnInfo, indica el nombre de la columna de la entidad
//@PrimaryKey(autoGenerate = true) indica la llave primaria de Nino, con autoincremento
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
