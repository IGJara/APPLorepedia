// com.example.applorepediakotlin.viewmodel/PersonajeViewModelFactory.kt

package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.applorepediakotlin.repository.PersonajeRepository

class PersonajeViewModelFactory(private val repository: PersonajeRepository) : ViewModelProvider.Factory {

    // Suprimimos el warning porque sabemos que el cast es seguro
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonajeViewModel::class.java)) {
            return PersonajeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}