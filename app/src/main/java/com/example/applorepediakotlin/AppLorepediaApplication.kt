// com.example.applorepediakotlin/AppLorepediaApplication.kt

package com.example.applorepediakotlin

import android.app.Application
import androidx.room.Room
import com.example.applorepediakotlin.api.ApiService
import com.example.applorepediakotlin.model.AppDatabase
import com.example.applorepediakotlin.repository.PersonajeRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppLorepediaApplication : Application() {

    // 1. Inicialización de Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            // USAMOS TU IP REAL (sin comillas extrañas)
            .baseUrl("http://192.168.18.196:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 2. Crear el ApiService
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // 3. Inicialización de Room
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "lorepedia-db"
        )
            // Esto ayuda a evitar errores si cambias la base de datos mientras pruebas
            .fallbackToDestructiveMigration()
            .build()
    }

    // 4. Crear el DAO
    val personajeDao by lazy {
        database.personajeDao()
    }

    // 5. Crear el Repositorio (Inyectando Room y Retrofit)
    val repository by lazy {
        PersonajeRepository(
            personajeDao,
            apiService,
            applicationContext // ✅ AÑADIR ESTO
        )
    }
}