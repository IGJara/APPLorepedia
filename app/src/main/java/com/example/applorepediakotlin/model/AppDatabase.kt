// com.example.applorepediakotlin.model/AppDatabase.kt (Corrección Necesaria)

package com.example.applorepediakotlin.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Asegúrate de que las entidades estén listadas y la versión sea correcta
@Database(entities = [PersonajeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // ⭐ 1. Definir el método para el DAO
    abstract fun personajeDao(): PersonajeDao // <--- Resuelve el error 'personajeDao'

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // ⭐ 2. Definir la función estática 'getDatabase'
        fun getDatabase(context: Context): AppDatabase { // <--- Resuelve el error 'getDatabase'
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lorepedia_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}