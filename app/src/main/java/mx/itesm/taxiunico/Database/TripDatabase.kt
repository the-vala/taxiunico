package mx.itesm.taxiunico.Database

import androidx.room.*
import android.content.Context

// @Database, Serves as the database holder an is the main accespoint to your relational data.
@Database(entities = arrayOf(Trip::class), version = 1, exportSchema = false)

abstract class TripDatabase : RoomDatabase() {

    /**
     * contains all your DAOs as abstract methods
      */

    abstract fun tripDao(): TripDao

    /**
     * using singleton object to handle the database and only one instance exists
     */
    companion object {

        private val DATABASE_NAME = "TripDB.db"
        private var dbInstance: TripDatabase? = null

        /**
         * getInstance. obtiene la instancia de la base de datos, en caso de no existir la crea
         */
        @Synchronized
        fun getInstance(context: Context): TripDatabase =
            dbInstance ?: buildDatabase(context).also { dbInstance = it}

        /**
         * buildDatabase. crea la base de datos con el nomnbre  especificado
         */
        private fun buildDatabase(context: Context): TripDatabase = Room.databaseBuilder(context,
            TripDatabase::class.java,
            DATABASE_NAME)
            .build()
    }
}