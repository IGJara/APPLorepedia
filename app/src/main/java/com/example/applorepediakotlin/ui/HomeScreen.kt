package com.example.applorepediakotlin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.R // Asegúrate de importar R
import com.example.applorepediakotlin.ui.theme.AppLopediaKotlinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToLista: () -> Unit) {
    Scaffold(
        // Barra superior con el nombre de la App
        topBar = { TopAppBar(title = { Text("AppLopedia Wiki") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // [ELEMENTO VISUAL] Usa un recurso de imagen
            // **Nota:** Debes tener un archivo en res/drawable llamado ic_launcher_foreground
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo de la Wiki",
                modifier = Modifier.size(150.dp)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Bienvenido a AppLopedia",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Descubre a tus personajes favoritos de videojuegos con información detallada y la opción de personalizar sus imágenes.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(48.dp))

            // [BOTÓN CON FUNCIÓN] Botón que activa la navegación (uso de botones con funciones) [cite: 45]
            Button(
                onClick = onNavigateToLista,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Ver Lista de Personajes", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// Previsualización de la pantalla principal
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppLopediaKotlinTheme {
        HomeScreen(onNavigateToLista = {})
    }
}