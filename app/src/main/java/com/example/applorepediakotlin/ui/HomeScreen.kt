package com.example.applorepediakotlin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout // Necesario para el nuevo botón
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.R
import com.example.applorepediakotlin.viewmodel.AuthViewModel
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    personajeViewModel: PersonajeViewModel,
    authViewModel: AuthViewModel,

    onNavigateToLista: () -> Unit,
    onLogout: () -> Unit
) {
    val isDarkTheme by personajeViewModel.isDarkTheme.collectAsState()
    val userName by authViewModel.currentUserName.collectAsState(initial = "Invitado")

    val welcomeMessage = if (userName == "Invitado" || userName.isNullOrEmpty()) {
        "Bienvenido a Lorepedia."
    } else {
        "Bienvenido, $userName."
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lorepedia - Inicio") },
                actions = {
                    // Botón de Tema (Mantenido)
                    IconButton(onClick = { personajeViewModel.toggleDarkTheme() }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.DarkMode,
                            contentDescription = "Cambiar Tema"
                        )
                    }
                    // ⭐ SE HA ELIMINADO EL BOTÓN DE CERRAR SESIÓN DE AQUÍ
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
            // Logo Decorativo
            Image(
                painter = painterResource(id = R.drawable.lorepedia_logo_text),
                contentDescription = "Logo de Lorepedia",
                modifier = Modifier.size(200.dp)
            )

            Spacer(Modifier.height(32.dp))

            // Bienvenida Personalizada
            Text(
                text = welcomeMessage,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Bienvenido a la Wiki de Personajes de videojuegos mas grande del mundo, ahora selecciona una opcion",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // --- BOTÓN 1: Lista de Personajes ---
            Button(
                onClick = onNavigateToLista,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.List, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Ver Lista de Personajes")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ⭐ BOTÓN 2: Cerrar Sesión (Nuevo Botón)
            OutlinedButton( // Usamos OutlinedButton para distinguirlo de la acción principal
                onClick = {
                    authViewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Logout, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}