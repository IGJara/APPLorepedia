// com.example.applorepediakotlin.viewmodel/PersonajeViewModel.kt

package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.repository.PersonajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class PersonajeViewModel(
    private val repository: PersonajeRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // Observamos directamente el Flow de Room (listaPersonajes)
    val listaPersonajes: StateFlow<List<Personaje>> = repository.personajes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        recargarDatos()
    }

    fun recargarDatos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.recargarPersonajes()
            } catch (e: Exception) {
                println("Error de carga de personajes: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para crear un personaje
    fun savePersonaje(personaje: Personaje) {
        viewModelScope.launch {
            repository.insertPersonaje(personaje)
        }
    }

    // Función para actualizar un personaje existente
    fun updatePersonaje(personaje: Personaje) {
        viewModelScope.launch {
            repository.insertPersonaje(personaje)
        }
    }

    // ⭐ NUEVA FUNCIÓN: Elimina un personaje
    fun deletePersonaje(personaje: Personaje) {
        viewModelScope.launch {
            repository.deletePersonaje(personaje)
        }
    }

    // Devolvemos el Flow que obtiene el detalle
    fun getPersonajeDetalleFlow(id: Int): StateFlow<Personaje?> = repository.obtenerPersonaje(id).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun getPersonajeDetalle(id: Int) = getPersonajeDetalleFlow(id)
}