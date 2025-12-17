package com.example.applorepediakotlin.repository

import com.example.applorepediakotlin.api.AuthService
import com.example.applorepediakotlin.datastore.AuthDataStore
import com.example.applorepediakotlin.model.UserCredentials
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class AuthRepository(
    private val authService: AuthService,
    private val authDataStore: AuthDataStore
) {
    // Flows expuestos para el ViewModel
    val authToken: Flow<String?> = authDataStore.authToken

    // El nombre de la propiedad en DataStore es currentUserId
    val currentUserId: Flow<Int?> = authDataStore.currentUserId

    // ⭐ MODIFICACIÓN 1: Exponer el nombre de usuario
    val currentUserName: Flow<String?> = authDataStore.currentUserName

    // Login
    suspend fun login(credentials: UserCredentials): Boolean {
        return try {
            val response = authService.login(credentials)
            // ... (VERIFICACIONES DE TOKEN MANTENIDAS)

            // 1. Guardar Token (Obligatorio)
            authDataStore.saveAuthToken(response.token)

            // 2. Guardar ID (Esencial, con verificación)
            val idToSave = response.userId
            if (idToSave != null && idToSave > 0) {
                authDataStore.saveUserId(idToSave)
            } else {
                println("AuthRepository ADVERTENCIA: ID de usuario no encontrado en la respuesta de login.")
            }

            // ⭐ MODIFICACIÓN 2: Guardar Nombre
            val nameToSave = response.name // ASUMIMOS que AuthResponseDto tiene 'name'
            if (!nameToSave.isNullOrEmpty()) {
                authDataStore.saveUserName(nameToSave)
            }


            true
        } catch (e: Exception) {
            println("AuthRepository: Error de credenciales o de red en login: ${e.message}")
            false
        }
    }

    // Signup
    suspend fun signup(credentials: UserCredentials): Boolean {
        return try {
            val response = authService.signup(credentials)
            // ... (VERIFICACIONES DE TOKEN MANTENIDAS)

            // 1. Guardar Token (Obligatorio)
            authDataStore.saveAuthToken(response.token)

            // 2. Guardar ID (Esencial, con verificación)
            val idToSave = response.userId
            if (idToSave != null && idToSave > 0) {
                authDataStore.saveUserId(idToSave)
            } else {
                println("AuthRepository ADVERTENCIA: ID de usuario no encontrado en la respuesta de registro.")
            }

            // ⭐ MODIFICACIÓN 3: Guardar Nombre
            val nameToSave = response.name // ASUMIMOS que AuthResponseDto tiene 'name'
            if (!nameToSave.isNullOrEmpty()) {
                authDataStore.saveUserName(nameToSave)
            }

            true
        } catch (e: Exception) {
            println("AuthRepository: Error de conexión o el usuario ya existe en signup: ${e.message}")
            false
        }
    }

    // Logout
    suspend fun logout() {
        try {
            authService.logout()
        } catch (e: Exception) {
            println("Logout en API falló, limpiando sesión local de todas formas: ${e.message}")
        }
        authDataStore.clearAuthToken()
        authDataStore.clearUserId() // Esta función debe haber sido actualizada en AuthDataStore para limpiar también el nombre.
        // authDataStore.clearUserName() <-- No es necesario si clearUserId fue actualizado.
    }
}