package com.example.applorepediakotlin.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@Composable
fun ThemeSwitcherButton(viewModel: PersonajeViewModel) {
    // Leer el estado actual del tema desde el ViewModel
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    // Determinar el icono a mostrar
    val icon = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode
    val contentDescription = if (isDarkTheme) "Cambiar a Tema Claro" else "Cambiar a Tema Oscuro"

    IconButton(
        onClick = { viewModel.toggleDarkTheme() } // Llama a la función del VM para cambiar el estado
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}