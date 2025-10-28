package com.example.applorepediakotlin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons // Importación necesaria para usar Icons.Default
import androidx.compose.material.icons.filled.LightMode // Importación para el ícono de modo claro
import androidx.compose.material.icons.filled.DarkMode // Importación para el ícono de modo oscuro
import androidx.compose.material.icons.filled.ArrowForwardIos // Importación para el ícono de flecha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.R
import com.example.applorepediakotlin.model.Personaje // <-- AGREGADO: Importación de la clase Personaje
import com.example.applorepediakotlin.ui.theme.AppLopediaKotlinTheme
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel // <-- AGREGADO: Importación de la clase PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeListScreen(
    viewModel: PersonajeViewModel,
    onPersonajeClick: (Int) -> Unit
) {
    // Observa la lista de personajes del ViewModel
    val personajes by viewModel.listaPersonajes.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                // Usa el nombre de la aplicación como título de la lista
                title = { Text(stringResource(R.string.app_name)) },
                // El botón de tema oscuro/claro también está disponible en esta vista
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
        if (personajes.isEmpty()) {
            // Mostrar un indicador de carga o mensaje si la lista está vacía
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(personajes) { personaje ->
                    PersonajeListItem(
                        personaje = personaje,
                        onClick = { onPersonajeClick(personaje.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PersonajeListItem(
    personaje: Personaje,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder para la imagen (no tenemos el detalle completo, así que usamos el icono de la app)
            // Cuando añadas imágenes reales a los personajes, reemplaza este Image
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(56.dp)
            ) {
                // Aquí deberías cargar la imagen del personaje si la tuvieras
                Text(
                    text = personaje.nombre.first().toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.wrapContentSize(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = personaje.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = personaje.juego,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Icono de flecha para indicar que es clickeable
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Para que esta Preview funcione, necesitarás una clase Personaje y una PersonajeViewModel
@Preview(showBackground = true)
@Composable
fun PersonajeListScreenPreview() {
    // Nota: Esta vista previa fallará si PersonajeListScreen o PersonajeDetailScreen no están definidas.
    // Solo es para verificar el diseño de la lista.
    AppLopediaKotlinTheme {
        // Necesita una implementación dummy para el preview
        // PersonajeListScreen(viewModel = PersonajeViewModel(), onPersonajeClick = {})
    }
}
