package com.example.applorepediakotlin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.applorepediakotlin.model.UserCredentials
import com.example.applorepediakotlin.viewmodel.AuthViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // ⭐ MODIFICACIÓN 1: Añadir el estado para el campo 'name'
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by authViewModel.isLoading.collectAsState(initial = false)
    val error by authViewModel.loginError.collectAsState(initial = null)

    val onRegister: () -> Unit = {
        // ⭐ MODIFICACIÓN 4: Usar el valor capturado de 'name'
        val credentials = UserCredentials(
            email = email,
            password = password,
            name = name // Usamos el valor del estado 'name' de la UI
        )
        authViewModel.signup(credentials, onSignupSuccess)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, enabled = !isLoading) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crear Nueva Cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(32.dp))

            // ⭐ MODIFICACIÓN 2: Insertar el campo de texto para el Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = !isLoading
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = onRegister,
                // ⭐ MODIFICACIÓN 3: El botón requiere que 'name' tampoco esté vacío
                enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrarse")
                }
            }
        }
    }
}