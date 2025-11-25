// com.example.applorepediakotlin.ui/CrearPersonajeScreen.kt (MODIFICADO)

package com.example.applorepediakotlin.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPersonajeScreen(
    viewModel: PersonajeViewModel,
    onBack: () -> Unit,
    onPersonajeGuardado: () -> Unit
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var juego by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    var showError by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imagenUri = uri
            } catch (e: Exception) {
                println("Error al persistir el permiso de URI: ${e.message}")
                imagenUri = uri
            }
        }
    }

    val onSave: () -> Unit = {
        // ⭐ CAMBIO CLAVE: La imagen ahora es opcional en la validación
        if (nombre.isBlank() || juego.isBlank() || descripcion.isBlank()) {
            showError = true
        } else {
            showError = false

            val nuevoPersonaje = Personaje(
                id = 0,
                nombre = nombre,
                juego = juego,
                descripcion = descripcion,
                // Si imagenUri es null, guardamos null.
                imagenUrl = imagenUri?.toString(),
                imagenResId = null
            )

            viewModel.savePersonaje(nuevoPersonaje)
            onPersonajeGuardado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Personaje") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = onSave) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar Personaje")
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

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Personaje") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                isError = showError && nombre.isBlank()
            )

            OutlinedTextField(
                value = juego,
                onValueChange = { juego = it },
                label = { Text("Juego/Franquicia") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                isError = showError && juego.isBlank()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(bottom = 16.dp),
                singleLine = false,
                isError = showError && descripcion.isBlank()
            )

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar Imagen (Opcional)") // Texto actualizado
            }

            Spacer(Modifier.height(16.dp))

            if (imagenUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imagenUri),
                    contentDescription = "Previsualización de imagen",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                )
            }

            if (showError) {
                Text(
                    "Todos los campos de texto son obligatorios.", // Mensaje actualizado
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}