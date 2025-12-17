package com.example.applorepediakotlin.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPersonajeScreen(
    viewModel: PersonajeViewModel,
    onBack: () -> Unit,
    onPersonajeGuardado: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var juego by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var musicaUrl by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    // Launcher para Imagen
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                imagenUri = it
            } catch (e: Exception) { imagenUri = it }
        }
    }

    // Launcher para Música (Corregido con OpenDocument)
    val musicLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) { e.printStackTrace() }
            musicaUrl = it.toString()
        }
    }

    val onSave: () -> Unit = {
        isSaving = true
        val nuevoPersonaje = Personaje(
            id = 0,
            nombre = nombre,
            juego = juego,
            descripcion = descripcion,
            musicaUrl = musicaUrl.ifBlank { null },
            imagenUrl = imagenUri?.toString(),
            imagenResId = null // Resolución del error inicial
        )

        scope.launch {
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = onSave, enabled = nombre.isNotBlank() && !isSaving) {
                        Icon(Icons.Filled.Save, null)
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = juego, onValueChange = { juego = it }, label = { Text("Juego") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))

            // Botón de Música
            Button(
                onClick = { musicLauncher.launch(arrayOf("audio/*")) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Icon(Icons.Filled.MusicNote, null)
                Spacer(Modifier.width(8.dp))
                Text(if (musicaUrl.isBlank()) "Seleccionar Música" else "Música Seleccionada ✅")
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = { galleryLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                Text("Seleccionar Imagen")
            }

            if (imagenUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imagenUri),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp).padding(top = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}