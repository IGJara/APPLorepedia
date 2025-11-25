// com.example.applorepediakotlin.ui/HomeScreen.kt (MODIFICADO)

package com.example.applorepediakotlin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.R
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PersonajeViewModel,
    onNavigateToLista: () -> Unit,
    onNavigateToEvaluacion: () -> Unit,
    // ⭐ NUEVA ACCIÓN DE NAVEGACIÓN
    onNavigateToCrearPersonaje: () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
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
                painter = painterResource(id = R.drawable.lorepedia_icon),
                contentDescription = "Logo de Lorepedia",
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
                text = "Descubre a tus personajes favoritos de videojuegos con información detallada y la opción de personalizar sus imágenes.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(48.dp))

            // ⭐ NUEVO BOTÓN PARA CREAR
            Button(
                onClick = onNavigateToCrearPersonaje,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Crear Personaje")
                Spacer(Modifier.width(8.dp))
                Text("Crear Nuevo Personaje", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateToLista,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Ver Lista de Personajes", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateToEvaluacion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Evaluar App")
                Spacer(Modifier.width(8.dp))
                Text("Evaluar la Aplicación", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}