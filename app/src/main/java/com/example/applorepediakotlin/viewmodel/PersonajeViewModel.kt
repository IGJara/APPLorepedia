package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.repository.PersonajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PersonajeViewModel : ViewModel() {
    private val repository = PersonajeRepository()

    // --- LÓGICA DEL MODO NOCTURNO ---
    // Estado que guarda la preferencia del tema.
    // Inicializado en TRUE para que el Modo Oscuro sea el default.
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Función que alterna el estado del tema
    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
    // ---------------------------------

    // Variable que contiene la lista de personajes.
    private val _listaPersonajes = MutableStateFlow(repository.obtenerTodosLosPersonajes())
    val listaPersonajes: StateFlow<List<Personaje>> = _listaPersonajes.asStateFlow()

    // Puedes agregar más lógica de negocio aquí (e.g., búsqueda, filtrado).
}
