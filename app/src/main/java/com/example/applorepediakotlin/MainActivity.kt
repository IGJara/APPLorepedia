package com.example.applorepediakotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.activity.viewModels
import com.example.applorepediakotlin.ui.NavegacionApp
import com.example.applorepediakotlin.ui.theme.AppLorepediaKotlinTheme
import com.example.applorepediakotlin.viewmodel.AuthViewModel
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel
import com.example.applorepediakotlin.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    private val applicationInstance by lazy { application as AppLorepediaApplication }

    // Utilizando el nombre de la clase 'ViewModelFactory' para obtener ViewModels
    // ⭐ CORRECCIÓN CRÍTICA: Usamos los getters públicos
    private val personajeViewModel: PersonajeViewModel by viewModels {
        ViewModelFactory(
            personajeRepository = applicationInstance.getPersonajeRepository(), // ⭐ USAR GETTER
            authRepository = applicationInstance.getAuthRepository() // ⭐ USAR GETTER
        )
    }

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(
            // ⭐ USAR GETTER
            personajeRepository = applicationInstance.getPersonajeRepository(),
            // ⭐ USAR GETTER
            authRepository = applicationInstance.getAuthRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ⭐ 1. LEER EL ESTADO DEL TEMA DESDE EL VIEWMODEL (Asumiendo que PersonajeViewModel lo maneja)
            val isDarkTheme by personajeViewModel.isDarkTheme.collectAsState()

            // ⭐ 2. PASAR EL ESTADO DEL TEMA AL COMPOSABLE DEL TEMA
            AppLorepediaKotlinTheme(
                darkTheme = isDarkTheme
            ) {
                NavegacionApp(
                    personajeViewModel = personajeViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}