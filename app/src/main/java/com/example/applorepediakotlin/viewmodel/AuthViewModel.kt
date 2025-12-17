package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applorepediakotlin.model.UserCredentials
import com.example.applorepediakotlin.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // ⭐ MODIFICACIÓN 1: Exponer el estado de autenticación (Mantenido)
    val isAuthenticated: StateFlow<Boolean> = authRepository.authToken
        .map { token ->
            !token.isNullOrEmpty()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // ⭐ MODIFICACIÓN 2: Exponer el nombre del usuario logueado
    val currentUserName: StateFlow<String?> = authRepository.currentUserName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun login(credentials: UserCredentials) {
        _loginError.value = null
        _isLoading.value = true
        viewModelScope.launch {
            val result = authRepository.login(credentials)
            if (!result) {
                _loginError.value = "Error de credenciales o de red. Intenta de nuevo."
            }
            _isLoading.value = false
        }
    }

    fun signup(credentials: UserCredentials, onSuccess: () -> Unit) {
        _loginError.value = null
        _isLoading.value = true
        viewModelScope.launch {
            val success = authRepository.signup(credentials)

            if (success) {
                onSuccess()
            } else {
                _loginError.value = "Registro fallido. El usuario ya existe o hay un error de red."
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}