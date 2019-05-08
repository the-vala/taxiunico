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