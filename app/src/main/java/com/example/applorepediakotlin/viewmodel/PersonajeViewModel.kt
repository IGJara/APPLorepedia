package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.model.toPersonajeDto // ⭐ NUEVO IMPORT
import com.example.applorepediakotlin.repository.PersonajeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonajeViewModel(
    private val repository: PersonajeRepository
) : ViewModel() {

    // ... (Lógica de tema y carga se mantiene)
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    fun toggleDarkTheme() { _isDarkTheme.value = !_isDarkTheme.value }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    val listaPersonajes: StateFlow<List<Personaje>> = combine(
        repository.personajes,
        _searchText
    ) { personajes, text ->
        if (text.isBlank()) {
            personajes
        } else {
            personajes.filter {
                it.nombre.contains(text, ignoreCase = true) ||
                        it.juego.contains(text, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun recargarDatos() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.recargarPersonajes() // Esto debe llamar a getPersonajes en el Repositorio
            _isLoading.value = false
        }
    }

    // ⭐ FUNCIÓN CORREGIDA: Ahora maneja la creación O la actualización vía API
    fun savePersonaje(personaje: Personaje) {
        viewModelScope.launch {
            try {
                val dto = personaje.toPersonajeDto()

                if (personaje.id == 0 || personaje.id == null) {
                    // Si ID es 0 o nulo, es una CREACIÓN (POST)
                    repository.createPersonaje(dto)
                } else {
                    // Si ID existe, es una ACTUALIZACIÓN (PUT/PATCH)
                    repository.updatePersonaje(dto)
                }

                // Después de la operación, recargamos los datos para actualizar la UI
                // repository.recargarPersonajes() // Si recargarPersonajes() lee de la API
                // O si PersonajeListScreen ya escucha el Flow, solo haz:
                // repository.insertPersonaje(personaje) // Si insertPersonaje actualiza la BD local

            } catch (e: Exception) {
                println("ViewModel Error al guardar/actualizar: ${e.message}")
                // Manejar error de UI aquí
            }
        }
    }

    // ⭐ FUNCIÓN ELIMINADA: La función insertPersonaje del repositorio se llama internamente.
    // fun insertPersonaje(personaje: Personaje) { ... }
    // La dejaremos si se requiere guardar en caché local DESPUÉS de la API.

    fun deletePersonaje(personaje: Personaje) {
        viewModelScope.launch {
            // Asumiendo que el repositorio tiene un deletePersonaje(id: Int) que llama a la API
            repository.deletePersonaje(personaje.id)
        }
    }

    fun getPersonajeDetalle(id: Int) = repository.getPersonajeDetalle(id)
}