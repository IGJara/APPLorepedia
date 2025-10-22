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
    // En MVVM, lo ideal es que el ViewModel tenga una función para obtener el personaje
    val personaje = viewModel.listaPersonajes.collectAsState().value.find { it.id == personajeId }

    // Estado local simulado para la imagen (simplificando la actualización en el ViewModel)
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // LANZADOR DE GALERÍA (TECNOLOGÍA DEL TELÉFONO)
    // Esto cumple con el requisito de usar al menos una tecnología del teléfono (galería)[cite: 39].
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
                .padding(16.dp)
        ) {
            // Muestra la imagen, si ha sido seleccionada
            AsyncImage(
                model = imageUri ?: personaje.imagenUrl,
                contentDescription = "Imagen de ${personaje.nombre}",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            // Botón para abrir la galería y cambiar la imagen
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar Imagen (Usar Galería)")
            }

            Spacer(Modifier.height(16.dp))

            Text(text = "Juego:", style = MaterialTheme.typography.titleMedium)
            Text(text = personaje.juego, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(8.dp))

            Text(text = "Descripción:", style = MaterialTheme.typography.titleMedium)
            Text(text = personaje.descripcion, style = MaterialTheme.typography.bodyLarge)
        }
    }
}