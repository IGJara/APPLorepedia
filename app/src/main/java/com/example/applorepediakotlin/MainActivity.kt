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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Obtenemos la instancia del ViewModel
            val viewModel: PersonajeViewModel = viewModel()

            // 2. Leemos el estado del tema desde el ViewModel
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            // 3. Aplicamos el tema usando el estado leído
            AppLopediaKotlinTheme(darkTheme = isDarkTheme) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
