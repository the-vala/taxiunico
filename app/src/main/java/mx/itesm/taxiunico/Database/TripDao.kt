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

// @Dao, In Room you use data access objects (DAO) to access and manage your data.
// The DAO is the main component of Room and includes methods that offer access
// to your apps database
@Dao
interface TripDao {

    // @Query, realiza una consulta en la base de datos
    @Query("SELECT * FROM Trip ORDER BY dateTime DESC")
    fun loadAllTrips():MutableList<Trip>

    // @Query, vacia la tabla
    @Query("DELETE FROM Trip")
    fun emptyTrips():Unit

    // @Insert, inserta uno u vairos elementos en la base de datos
    @Insert
    fun insertAllTrips(tripList: List<Trip>)

}