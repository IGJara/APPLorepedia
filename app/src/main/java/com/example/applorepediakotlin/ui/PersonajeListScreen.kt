// com.example.applorepediakotlin.ui/PersonajeListScreen.kt (FINALIZADO)

package com.example.applorepediakotlin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.* // ⭐ Importante para remember y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeListScreen(
    viewModel: PersonajeViewModel,
    onPersonajeClick: (Int) -> Unit
) {
    val listaPersonajes by viewModel.listaPersonajes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // ⭐ ESTADOS PARA CONTROLAR EL DIÁLOGO DE ELIMINACIÓN
    val showDeleteDialog = remember { mutableStateOf(false) }
    val personajeToDelete = remember { mutableStateOf<Personaje?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lorepedia") })
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaPersonajes) { personaje ->
                    PersonajeListItem(
                        personaje = personaje,
                        onPersonajeClick = onPersonajeClick,
                        // ⭐ Al hacer clic en eliminar, actualizamos los estados del diálogo
                        onDeleteClick = { p ->
                            personajeToDelete.value = p
                            showDeleteDialog.value = true
                        }
                    )
                }
                if (listaPersonajes.isEmpty()) {
                    item {
                        Text(
                            text = "No hay personajes. Crea uno nuevo desde la pantalla de inicio.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

    // ⭐ LLAMADA AL DIÁLOGO
    if (showDeleteDialog.value && personajeToDelete.value != null) {
        DeleteConfirmationDialog(
            personaje = personajeToDelete.value!!,
            onConfirmDelete = { p ->
                viewModel.deletePersonaje(p)
                showDeleteDialog.value = false
                personajeToDelete.value = null
            },
            onDismiss = {
                showDeleteDialog.value = false
                personajeToDelete.value = null
            }
        )
    }
}

// ======================================================================
// ⭐ NUEVA FUNCIÓN COMPOSABLE: Diálogo de Confirmación de Eliminación
// ======================================================================

@Composable
fun DeleteConfirmationDialog(
    personaje: Personaje,
    onConfirmDelete: (Personaje) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Cierra al tocar fuera o presionar atrás
        title = {
            Text("Confirmar Eliminación")
        },
        text = {
            Text("¿Estás seguro de que deseas eliminar a ${personaje.nombre}? Esta acción no se puede deshacer.")
        },
        confirmButton = {
            Button(
                onClick = { onConfirmDelete(personaje) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar", color = MaterialTheme.colorScheme.onError)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )

}


// ======================================================================
// Componente de Item de la Lista (se mantiene igual, solo se cambia la llamada)
// ======================================================================

@Composable
fun PersonajeListItem(
    personaje: Personaje,
    onPersonajeClick: (Int) -> Unit,
    onDeleteClick: (Personaje) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPersonajeClick(personaje.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // IMAGEN DE PERFIL O PLACEHOLDER (SIN CAMBIOS)
            if (!personaje.imagenUrl.isNullOrBlank()) {
                AsyncImage(
                    model = personaje.imagenUrl,
                    contentDescription = "Imagen de ${personaje.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            } else {
                Surface(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = personaje.nombre.firstOrNull()?.uppercaseChar().toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = personaje.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = personaje.juego,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // BOTÓN DE ELIMINAR (LLAMA AL MANEJADOR DE DIÁLOGO)
            IconButton(onClick = { onDeleteClick(personaje) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar ${personaje.nombre}",
                    tint = MaterialTheme.colorScheme.error
                )
            }

            // ICONO DE DETALLE
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver detalle",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}