package com.example.applorepediakotlin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.applorepediakotlin.ui.theme.AppLopediaKotlinTheme
import com.example.applorepediakotlin.viewmodel.EvaluacionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionScreen(
    viewModel: EvaluacionViewModel = viewModel(), // Usamos el VM inyectado por Compose
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Evaluación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tu opinión nos ayuda a mejorar AppLopedia",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- 1. CAMPO NOMBRE ---
            OutlinedTextField(
                value = viewModel.nombreEvaluador,
                onValueChange = viewModel::updateNombre,
                label = { Text("Tu Nombre (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. EVALUACIÓN DE DISEÑO (SLIDER) ---
            Text(
                text = "Calificación de Diseño: ${viewModel.calificacionDiseno.toInt()} / 10",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Slider(
                value = viewModel.calificacionDiseno,
                onValueChange = viewModel::updateDiseno,
                valueRange = 0f..10f,
                steps = 9, // Permite valores enteros de 0 a 10
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. EVALUACIÓN DE FUNCIONALIDAD (SLIDER) ---
            Text(
                text = "Calificación de Funcionalidad: ${viewModel.calificacionFuncionalidad.toInt()} / 10",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Slider(
                value = viewModel.calificacionFuncionalidad,
                onValueChange = viewModel::updateFuncionalidad,
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. CAMPO COMENTARIO ---
            OutlinedTextField(
                value = viewModel.comentario,
                onValueChange = viewModel::updateComentario,
                label = { Text("Comentarios Adicionales") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 5. BOTÓN DE ENVÍO ---
            if (viewModel.isSubmitted) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¡Gracias! Evaluación enviada.",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = viewModel::resetSubmissionStatus) {
                        Text("Evaluar de Nuevo")
                    }
                }
            } else {
                Button(
                    onClick = viewModel::submitFormulario,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    // Deshabilitar el botón si no se ha escrito al menos un nombre o comentario
                    enabled = viewModel.nombreEvaluador.isNotBlank() || viewModel.comentario.isNotBlank(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Enviar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar Evaluación")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EvaluacionScreenPreview() {
    AppLopediaKotlinTheme {
        // En el preview usamos una instancia nueva
        EvaluacionScreen(onBack = {})
    }
}
