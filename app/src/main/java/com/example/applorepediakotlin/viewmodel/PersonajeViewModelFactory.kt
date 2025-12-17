package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.applorepediakotlin.repository.AuthRepository
import com.example.applorepediakotlin.repository.PersonajeRepository

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val personajeRepository: PersonajeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PersonajeViewModel::class.java) -> {
                PersonajeViewModel(personajeRepository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}