package com.example.applorepediakotlin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit // Importación necesaria para el nuevo botón
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
// Se actualiza la firma de la función con el nuevo destino de navegación
fun HomeScreen(
    viewModel: PersonajeViewModel,
    onNavigateToLista: () -> Unit,
    onNavigateToEvaluacion: () -> Unit // Nuevo parámetro para navegar a la evaluación
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AppLopedia Wiki") },
                actions = {
                    // Botón de Modo Oscuro/Claro
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
            verticalArrangement = Arrangement.Center // Centra el contenido verticalmente
        ) {
            Image(
                // Asegúrate de que R.drawable.ic_launcher_foreground existe en tu proyecto
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo de la Wiki",
                modifier = Modifier.size(150.dp)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Bienvenido a AppLopedia",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            // Texto descriptivo mejorado
            Text(
                text = "Descubre a tus personajes favoritos de videojuegos con información detallada y la opción de personalizar sus imágenes.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(48.dp))

            // Botón principal para la lista de personajes
            Button(
                onClick = onNavigateToLista,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Ver Lista de Personajes", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))

            // --- NUEVO BOTÓN DE EVALUACIÓN ---
            OutlinedButton(
                onClick = onNavigateToEvaluacion, // Llama a la nueva función de navegación
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Evaluar App")
                Spacer(Modifier.width(8.dp))
                Text("Evaluar la Aplicación", style = MaterialTheme.typography.titleMedium)
            }
            // ---------------------------------

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppLopediaKotlinTheme {
        // Se debe actualizar el Preview con el nuevo argumento
        HomeScreen(viewModel = PersonajeViewModel(), onNavigateToLista = {}, onNavigateToEvaluacion = {})
    }
}
