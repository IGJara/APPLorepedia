package com.example.applorepediakotlin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
// ⭐ NUEVOS IMPORTS
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import com.example.applorepediakotlin.ui.ThemeSwitcherButton
// ⭐ FIN NUEVOS IMPORTS
import androidx.compose.material3.*
import androidx.compose.runtime.* import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeListScreen(
    viewModel: PersonajeViewModel,
    onPersonajeClick: (Int) -> Unit,
    // ⭐ AÑADIR PARÁMETRO DE NAVEGACIÓN (Punto 5)
    onNavigateToCrearPersonaje: () -> Unit
) {
    // ESTADOS DEL VIEWMODEL
    val listaPersonajes by viewModel.listaPersonajes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    val showDeleteDialog = remember { mutableStateOf(false) }
    val personajeToDelete = remember { mutableStateOf<Personaje?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lorepedia") },
                actions = {
                    // ⭐ IMPLEMENTACIÓN DEL TEMA UNIVERSAL (Punto 6)
                    ThemeSwitcherButton(viewModel = viewModel)
                }
            )
        },
        // ⭐ IMPLEMENTACIÓN DEL FAB PARA CREAR PERSONAJE (Punto 5)
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCrearPersonaje) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Personaje")
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
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

                // ⭐ 1. ENCABEZADO DE INFORMACIÓN
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                        Text(
                            text = "Lista de Personajes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Contenido sincronizado desde Xano. Total: ${listaPersonajes.size}",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // ⭐ 2. CAMPO DE FILTRADO
                item {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = viewModel::onSearchTextChange,
                        label = { Text("Buscar Personaje o Juego") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        singleLine = true
                    )
                }

                // Lista de personajes filtrada
                items(listaPersonajes, key = { it.id }) { personaje ->
                    PersonajeListItem(
                        personaje = personaje,
                        onPersonajeClick = onPersonajeClick,
                        onDeleteClick = { p ->
                            personajeToDelete.value = p
                            showDeleteDialog.value = true
                        }
                    )
                }

                // MENSAJES DE LISTA VACÍA
                if (listaPersonajes.isEmpty() && searchText.isBlank()) {
                    item {
                        Text(
                            // ⭐ MENSAJE ACTUALIZADO PARA USAR EL FAB
                            text = "No hay personajes. Usa el botón '+' para crear uno nuevo.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else if (listaPersonajes.isEmpty() && searchText.isNotBlank()) {
                    item {
                        Text(
                            text = "No se encontraron resultados para '${searchText}'.",
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

    // ⭐ LLAMADA AL DIÁLOGO (Mantenido)
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
// Componentes Secundarios (Mantienen la lógica de eliminación)
// ======================================================================

@Composable
fun DeleteConfirmationDialog(
    personaje: Personaje,
    onConfirmDelete: (Personaje) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Eliminación") },
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
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // IMAGEN DE PERFIL O PLACEHOLDER
            if (!personaje.imagenUrl.isNullOrBlank()) {
                AsyncImage(
                    model = personaje.imagenUrl,
                    contentDescription = "Imagen de ${personaje.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp).clip(CircleShape)
                )
            } else {
                Surface(
                    modifier = Modifier.size(50.dp).clip(CircleShape),
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

            Column(modifier = Modifier.weight(1f)) {
                Text(text = personaje.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = personaje.juego, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // BOTÓN DE ELIMINAR
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