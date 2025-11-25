package com.example.applorepediakotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applorepediakotlin.ui.AppNavigation
import com.example.applorepediakotlin.ui.theme.AppLopediaKotlinTheme
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel
import com.example.applorepediakotlin.viewmodel.PersonajeViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // 1. Obtener la instancia de la aplicación
            val application = application as AppLorepediaApplication

            // 2. Crear la Factoría (Factory) con el Repositorio
            val factory = PersonajeViewModelFactory(application.repository)

            // 3. Crear el ViewModel usando la Factoría
            val viewModel: PersonajeViewModel = viewModel(factory = factory)

            // 4. Leer el estado del tema
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            // 5. Aplicar el tema e iniciar la navegación
            AppLopediaKotlinTheme(darkTheme = isDarkTheme) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}