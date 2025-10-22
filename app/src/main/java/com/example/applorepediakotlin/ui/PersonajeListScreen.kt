package com.example.applorepediakotlin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.model.Personaje
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeListScreen(
    viewModel: PersonajeViewModel,
    onPersonajeClick: (Int) -> Unit
) {
    // Escucha el estado del ViewModel
    val personajes by viewModel.listaPersonajes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Wiki de Personajes") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
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

@Composable
fun PersonajeListItem(personaje: Personaje, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick) // Uso de botones o formularios con funciones [cite: 45]
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = personaje.nombre, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Juego: ${personaje.juego}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}