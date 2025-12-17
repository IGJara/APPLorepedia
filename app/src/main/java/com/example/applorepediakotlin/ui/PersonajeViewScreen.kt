package com.example.applorepediakotlin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeViewScreen(
    viewModel: PersonajeViewModel,
    personajeId: Int,
    onBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val personajeFlow = remember(personajeId) { viewModel.getPersonajeDetalle(personajeId) }
    val personaje by personajeFlow.collectAsState(initial = null)

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val audioPlayerManager = remember { AudioPlayerManager(context) }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(audioPlayerManager)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(audioPlayerManager)
            audioPlayerManager.stop()
        }
    }

    LaunchedEffect(personaje) {
        personaje?.musicaUrl?.takeIf { it.isNotBlank() }?.let { url ->
            // Se puede activar si quieres auto-reproducir
            // audioPlayerManager.play(url)
        }
    }

    if (personaje == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    personaje?.let { p ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(p.nombre) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onNavigateToEdit(p.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = p.imagenUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.padding(vertical = 16.dp).height(250.dp).fillMaxWidth()
                )

                p.musicaUrl?.takeIf { it.isNotBlank() }?.let { url ->
                    val isPlaying = audioPlayerManager.isAudioPlaying

                    Button(
                        onClick = { audioPlayerManager.togglePlayback(url) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(if (isPlaying) Icons.Filled.MusicOff else Icons.Filled.MusicNote, null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (isPlaying) "Detener Música" else "Reproducir Música")
                    }
                }

                Text(p.nombre, style = MaterialTheme.typography.headlineLarge)
                Text("Juego: ${p.juego}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
                Text(p.descripcion, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}