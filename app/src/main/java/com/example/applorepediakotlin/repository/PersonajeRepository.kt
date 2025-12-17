package com.example.applorepediakotlin.repository

import com.example.applorepediakotlin.api.PersonajeService
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.model.PersonajeDto
import com.example.applorepediakotlin.model.toDomain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

// ⭐ IMPORTS CRÍTICOS DE COROUTINES/FLOW
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow // <-- ¡ESTA ES LA IMPORTACIÓN FALTANTE!
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import java.lang.IllegalStateException
import java.lang.IllegalArgumentException

class PersonajeRepository(
    private val personajeService: PersonajeService,
    private val authRepository: AuthRepository
) {
    // Función que carga los personajes de la API (será usada para emitir el Flow)
    // ⭐ CORRECCIÓN: Definir el tipo de retorno explícitamente para evitar ambigüedad en el compilador
    private fun fetchPersonajesFromApi(): Flow<List<Personaje>> = flow {
        try {
            val personajesDto = personajeService.getPersonajes()
            val personajesDomain = personajesDto.map { it.toDomain() }
            emit(personajesDomain)
        } catch (e: Exception) {
            println("PersonajeRepository: Error al cargar personajes: ${e.message}")
            emit(emptyList())
        }
    }

    // ⭐ PROPIEDAD PARA QUE EL VIEWMODEL OBSERVE
    private val _personajes = MutableStateFlow<List<Personaje>>(emptyList())
    val personajes: StateFlow<List<Personaje>> = _personajes.asStateFlow()

    init {
        // Cargar los datos al inicio
        recargarPersonajes()
    }

    // CRÍTICO: Función para crear un personaje, inyectando el User ID.
    suspend fun createPersonaje(personaje: PersonajeDto): PersonajeDto {
        val currentUserId = authRepository.currentUserId.first()
        if (currentUserId == null) {
            throw IllegalStateException("No se puede crear un personaje: usuario no autenticado.")
        }
        val dtoToSend = personaje.copy(userId = currentUserId)
        return personajeService.createPersonaje(dtoToSend).also { recargarPersonajes() }
    }

    // Función para actualizar un personaje existente (PUT/PATCH)
    suspend fun updatePersonaje(personajeDto: PersonajeDto): PersonajeDto {
        val id = personajeDto.id
        if (id == null || id <= 0) {
            throw IllegalArgumentException("El ID del personaje es necesario para la actualización.")
        }
        return personajeService.updatePersonaje(id, personajeDto).also { recargarPersonajes() }
    }

    // Función para obtener el detalle de un personaje por ID
    fun getPersonajeDetalle(id: Int): Flow<Personaje?> = flow {
        try {
            val personajeDto = personajeService.getPersonajeById(id)
            emit(personajeDto.toDomain())
        } catch (e: Exception) {
            println("Error al obtener detalle de personaje: ${e.message}")
            emit(null)
        }
    }

    // Función para eliminar
    suspend fun deletePersonaje(id: Int) {
        personajeService.deletePersonaje(id)
        recargarPersonajes()
    }

    // FUNCIÓN CORREGIDA: Ahora actualiza el StateFlow interno (_personajes)
    fun recargarPersonajes() {
        // Recolectar el Flow de la API y actualizar el StateFlow interno
        fetchPersonajesFromApi().onEach {
            _personajes.value = it
        }.launchIn(GlobalScope) // Se lanza en el GlobalScope ya que el Repositorio no tiene un Scope propio
    }
}