package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.repository.PersonajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PersonajeViewModel : ViewModel() {
    private val repository = PersonajeRepository()

    // Variable que contiene la lista de personajes.
    // Esto cumple con el requisito de tener al menos una variable[cite: 41].
    private val _listaPersonajes = MutableStateFlow(repository.obtenerTodosLosPersonajes())
    val listaPersonajes: StateFlow<List<Personaje>> = _listaPersonajes.asStateFlow()

    // Puedes agregar más lógica de negocio aquí (e.g., búsqueda, filtrado).
}