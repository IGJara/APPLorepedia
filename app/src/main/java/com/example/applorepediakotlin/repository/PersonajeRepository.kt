// com.example.applorepediakotlin.repository/PersonajeRepository.kt

package com.example.applorepediakotlin.repository

import android.content.Context
import com.example.applorepediakotlin.api.ApiService
import com.example.applorepediakotlin.api.LocalDataSource
import com.example.applorepediakotlin.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class PersonajeRepository(
    private val personajeDao: PersonajeDao,
    private val apiService: ApiService,
    private val applicationContext: Context
) {

    // PARTE 1: FLUJO DE DATOS (OBSERVABLE)
    val personajes: Flow<List<Personaje>> = personajeDao.getAllPersonajes()
        .map { entities ->
            entities.map { it.toDomain() }
        }

    // OPERACIÓN DE CARGA INICIAL
    suspend fun recargarPersonajes() {
        withContext(Dispatchers.IO) {
            try {
                val localList = LocalDataSource.loadPersonajesFromAssets(applicationContext)

                if (localList.isNotEmpty()) {
                    personajeDao.insertAll(localList.map { it.toEntity() })
                } else {
                    println("Error: La lista local está vacía o hubo un error de parseo.")
                }
            } catch (e: Exception) {
                println("Error al procesar datos locales: ${e.message}")
            }
        }
    }

    // Obtener detalle
    fun obtenerPersonaje(id: Int): Flow<Personaje?> {
        return personajeDao.getPersonajeById(id).map { it?.toDomain() }
    }

    // Función para crear/actualizar un personaje
    suspend fun insertPersonaje(personaje: Personaje) {
        withContext(Dispatchers.IO) {
            personajeDao.insertPersonaje(personaje.toEntity())
        }
    }

    // ⭐ NUEVA FUNCIÓN: Elimina un personaje
    suspend fun deletePersonaje(personaje: Personaje) {
        withContext(Dispatchers.IO) {
            // Convierte el modelo de dominio a entidad y llama al DAO para eliminar
            personajeDao.deletePersonaje(personaje.toEntity())
        }
    }
}