package mx.itesm.taxiunico.Database

import androidx.room.*
import android.content.Context

@Database(entities = arrayOf(Trip::class), version = 1, exportSchema = false)

abstract class TripDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao

    companion object {

        private val DATABASE_NAME = "TripDB.db"
        private var dbInstance: TripDatabase? = null

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