// com.example.applorepediakotlin.ui/PersonajeDetailScreen.kt

package com.example.applorepediakotlin.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel
import coil.compose.AsyncImage
import com.example.applorepediakotlin.model.Personaje

// ⭐ NUEVAS IMPORTACIONES REQUERIDAS PARA PERSISTENCIA DE URI
import android.content.Intent
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeDetailScreen(
    viewModel: PersonajeViewModel,
    personajeId: Int,
    onBack: () -> Unit
) {
    // Flow recordado (corrección de bucle anterior)
    val personajeFlow = remember(personajeId) {
        viewModel.getPersonajeDetalle(personajeId)
    }
    val personaje by personajeFlow.collectAsState(initial = null)

    // Obtener el contexto para la gestión de permisos
    val context = LocalContext.current

    // Estado local simulado para la imagen de la galería (mantenido pero no usado en la carga principal)
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    // LANZADOR DE GALERÍA (TECNOLOGÍA DEL TELÉFONO)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && personaje != null) {

            // ⭐ LÓGICA CLAVE: Persistir el permiso de URI
            try {
                // Pedimos permiso para leer esta URI incluso después del cierre de la app
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                println("Error al persistir el permiso de URI en detalle: ${e.message}")
            }

            // 1. Crear un nuevo objeto Personaje con la URI de imagen actualizada
            val updatedPersonaje = personaje!!.copy(
                imagenUrl = uri.toString() // Guardamos la nueva URI local como String
            )

            // 2. GUARDAR LA MODIFICACIÓN EN LA BASE DE DATOS
            viewModel.updatePersonaje(updatedPersonaje)
        }
    }

    // Manejo de la carga o si el personaje es nulo
    if (personaje == null) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(32.dp))
            Text("Cargando detalle del Personaje ID: $personajeId")
        }
        return
    }

    // Usamos 'p' para referirnos al personaje no nulo
    personaje?.let { p ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(p.nombre) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // CARGA DE IMAGEN: Usa la URI de la base de datos (p.imagenUrl)
                AsyncImage(
                    // La fuente de la imagen ahora es el URL guardado en la base de datos (p.imagenUrl)
                    model = p.imagenUrl,
                    contentDescription = "Imagen de ${p.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .height(200.dp)
                        .fillMaxWidth(0.8f)
                )

                // Botón para abrir la galería
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambiar Imagen (Usar Galería)")
                }

                Spacer(Modifier.height(24.dp))

                Text(text = "Juego:", style = MaterialTheme.typography.titleMedium)
                Text(text = p.juego, style = MaterialTheme.typography.bodyLarge)

                Spacer(Modifier.height(16.dp))

                Text(text = "Descripción:", style = MaterialTheme.typography.titleMedium)
                Text(text = p.descripcion, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}