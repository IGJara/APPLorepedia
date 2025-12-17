package com.example.applorepediakotlin.ui

import android.net.Uri
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeEditScreen(
    viewModel: PersonajeViewModel,
    personajeId: Int,
    onBack: () -> Unit
) {
    val personajeFlow = remember(personajeId) { viewModel.getPersonajeDetalle(personajeId) }
    val personaje by personajeFlow.collectAsState(initial = null)
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var juego by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var musicaUrl by remember { mutableStateOf("") }
    var newImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(personaje) {
        personaje?.let {
            nombre = it.nombre
            juego = it.juego
            descripcion = it.descripcion
            musicaUrl = it.musicaUrl ?: ""
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) { e.printStackTrace() }
            newImageUri = it
        }
    }

    // ⭐ CORRECCIÓN: OpenDocument para evitar SecurityException
    val musicLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                // Esto permite que el audio funcione incluso después de reiniciar el teléfono
                context.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                println("Aviso: No se pudo persistir el permiso: ${e.message}")
            }
            musicaUrl = it.toString()
        }
    }

    fun saveChanges() {
        personaje?.let { p ->
            val updated = p.copy(
                nombre = nombre,
                juego = juego,
                descripcion = descripcion,
                musicaUrl = musicaUrl.ifBlank { null },
                imagenUrl = newImageUri?.toString() ?: p.imagenUrl
            )
            viewModel.savePersonaje(updated)
            onBack()
        }
    }

    if (personaje == null) return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Personaje") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    Button(onClick = { saveChanges() }, enabled = nombre.isNotBlank()) {
                        Text("Guardar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = newImageUri ?: personaje?.imagenUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(200.dp).fillMaxWidth().padding(bottom = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { imageLauncher.launch("image/*") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PhotoLibrary, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Imagen")
                }

                Button(
                    // ⭐ CORRECCIÓN: OpenDocument requiere un Array de MimeTypes
                    onClick = { musicLauncher.launch(arrayOf("audio/*")) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(Icons.Filled.MusicNote, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Audio")
                }
            }

            if (musicaUrl.isNotBlank()) {
                Text("Audio seleccionado ✅", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = juego, onValueChange = { juego = it }, label = { Text("Juego") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(150.dp))
        }
    }
}