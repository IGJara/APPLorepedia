package com.example.applorepediakotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel // <-- ¡Nueva importación clave!
import com.example.applorepediakotlin.ui.AppNavigation
import com.example.applorepediakotlin.ui.theme.AppLopediaKotlinTheme
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

class MainActivity : ComponentActivity() {

    // -----------------------------------------------------------------
    // | ELIMINAMOS LA DECLARACIÓN CON DELEGATE PARA EVITAR CONFLICTOS |
    // -----------------------------------------------------------------
    // private val viewModel: PersonajeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Obtenemos la instancia del ViewModel usando la función de Compose
            // El ciclo de vida está automáticamente gestionado aquí.
            val viewModel: PersonajeViewModel = viewModel()

            AppLopediaKotlinTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}