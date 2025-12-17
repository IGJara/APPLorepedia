// Archivo: com.example.applorepediakotlin/AppLorepediaApplication.kt (COMPLETO Y CORREGIDO)

package com.example.applorepediakotlin

import android.app.Application
import com.example.applorepediakotlin.api.AuthService
import com.example.applorepediakotlin.api.PersonajeService
// ⭐ AÑADIR LAS CONSTANTES DE URL
import com.example.applorepediakotlin.api.AUTH_BASE_URL
import com.example.applorepediakotlin.api.PERSONAJE_BASE_URL
import com.example.applorepediakotlin.api.createOkHttpClient
import com.example.applorepediakotlin.api.createRetrofit // Ahora acepta la URL base
import com.example.applorepediakotlin.api.createService
import com.example.applorepediakotlin.datastore.AuthDataStore
import com.example.applorepediakotlin.repository.AuthRepository
import com.example.applorepediakotlin.repository.PersonajeRepository

// ⭐ IMPORTACIONES DEL ROOM/DAO (Mantenidas)
import com.example.applorepediakotlin.model.PersonajeDao
import com.example.applorepediakotlin.model.AppDatabase

class AppLorepediaApplication : Application() {

    // --- INSTANCIAS DE DATASTORE ---
    private lateinit var authDataStore: AuthDataStore

    // --- INSTANCIAS DE SERVICIO (API) ---
    private lateinit var authService: AuthService
    private lateinit var personajeService: PersonajeService

    // --- INSTANCIAS DE REPOSITORIO ---
    private lateinit var authRepository: AuthRepository
    private lateinit var personajeRepository: PersonajeRepository

    // --- INSTANCIAS DE BASE DE DATOS LOCAL (Si se usa en el futuro) ---
    private lateinit var database: AppDatabase
    private lateinit var personajeDao: PersonajeDao

    override fun onCreate() {
        super.onCreate()

        // 0. Inicializar Base de Datos (Room) y DAO
        database = AppDatabase.getDatabase(applicationContext)
        personajeDao = database.personajeDao()

        // 1. Inicializar DataStore
        authDataStore = AuthDataStore(applicationContext)

        // 2. Inicializar Retrofit y Servicios (¡CORRECCIÓN CLAVE AQUÍ!)
        val okHttpClient = createOkHttpClient(authDataStore)

        // ⭐ CREAR INSTANCIA DE RETROFIT PARA AUTENTICACIÓN
        val authRetrofit = createRetrofit(okHttpClient, AUTH_BASE_URL)
        authService = createService<AuthService>(authRetrofit)

        // ⭐ CREAR INSTANCIA DE RETROFIT PARA PERSONAJES
        val personajeRetrofit = createRetrofit(okHttpClient, PERSONAJE_BASE_URL)
        personajeService = createService<PersonajeService>(personajeRetrofit)


        // 3. Inicializar Repositorios

        // Inicializar AuthRepository
        authRepository = AuthRepository(
            authService = authService,
            authDataStore = authDataStore
        )

        // Inicializar PersonajeRepository
        personajeRepository = PersonajeRepository(
            personajeService = personajeService,
            authRepository = authRepository
        )
    }

    // --- Getter para Repositorios (para usar en ViewModels) ---

    fun getAuthRepository(): AuthRepository = authRepository

    fun getPersonajeRepository(): PersonajeRepository = personajeRepository
}