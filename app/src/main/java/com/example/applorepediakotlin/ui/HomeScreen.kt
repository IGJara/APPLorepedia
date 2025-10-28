package com.example.applorepediakotlin.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.R
import com.example.applorepediakotlin.ui.theme.AppLopediaKotlinTheme
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Modificamos para aceptar el ViewModel
fun HomeScreen(viewModel: PersonajeViewModel, onNavigateToLista: () -> Unit) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState() // Leemos el estado del tema

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lorepedia") },
                // --- BOTÓN DE MODO NOCTURNO ---
                actions = {
                    IconButton(onClick = { viewModel.toggleDarkTheme() }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkTheme) "Cambiar a modo claro" else "Cambiar a modo oscuro"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                // Asegúrate de que este recurso exista en tu carpeta res/drawable
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo de la Wiki",
                modifier = Modifier.size(150.dp)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Bienvenido a Lorepedia",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Descubre a tus personajes favoritos de videojuegos con información detallada.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(48.dp))

            Button(
                onClick = onNavigateToLista,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Ver Lista de Personajes", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppLopediaKotlinTheme {
        HomeScreen(viewModel = PersonajeViewModel(), onNavigateToLista = {})
    }
}
