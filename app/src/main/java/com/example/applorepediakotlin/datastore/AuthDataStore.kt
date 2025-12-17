// Archivo: com.example.applorepediakotlin.datastore/AuthDataStore.kt (CORREGIDO)

package com.example.applorepediakotlin.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthDataStore(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val USER_ID_KEY = intPreferencesKey("user_id")
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

        // ⭐ CORRECCIÓN 1: Clave para guardar el Nombre de Usuario
        val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    // --- Lógica del Token de Autenticación (Mantenida) ---

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    val authToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }

    suspend fun clearAuthToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    // --- Lógica del ID del Usuario (Mantenida) ---

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    val currentUserId: Flow<Int?> = dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    // ⭐ CORRECCIÓN 2: Lógica para guardar el Nombre de Usuario
    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // ⭐ CORRECCIÓN 3: Propiedad para leer el Nombre de Usuario
    val currentUserName: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY]
        }

    // ⭐ CORRECCIÓN 4: Actualizar la limpieza para incluir el Nombre de Usuario
    suspend fun clearUserId() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_NAME_KEY) // Limpieza del nombre
        }
    }
}