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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeDetailScreen(
    viewModel: PersonajeViewModel,
    personajeId: Int,
    onBack: () -> Unit
) {
    // Obteniendo el personaje del ViewModel
    val personaje = viewModel.listaPersonajes.collectAsState().value.find { it.id == personajeId }

    // Estado local simulado para la imagen de la galería
    // (En una app real, esta URI debería persistirse en el ViewModel y base de datos)
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // LANZADOR DE GALERÍA (TECNOLOGÍA DEL TELÉFONO)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri // Guarda la URI de la imagen seleccionada
    }

    if (personaje == null) {
        Text("Personaje no encontrado", modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(personaje.nombre) },
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
            horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
        ) {

            // CARGA DE IMAGEN: Prioriza la imagen de galería, sino usa el recurso local
            AsyncImage(
                // Usa la URI de la galería o el ID del recurso local
                model = imageUri ?: personaje.imagenResId,
                contentDescription = "Imagen de ${personaje.nombre}",
                contentScale = ContentScale.Crop, // Escala la imagen para que llene el espacio
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(200.dp)
                    .fillMaxWidth(0.8f) // Ocupa el 80% del ancho
            )

            // Botón para abrir la galería y cambiar la imagen
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar Imagen (Usar Galería)")
            }

            Spacer(Modifier.height(24.dp)) // Aumentado el espacio para mejor separación

            Text(text = "Juego:", style = MaterialTheme.typography.titleMedium)
            Text(text = personaje.juego, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(16.dp))

            Text(text = "Descripción:", style = MaterialTheme.typography.titleMedium)
            Text(text = personaje.descripcion, style = MaterialTheme.typography.bodyLarge)
        }
    }
}