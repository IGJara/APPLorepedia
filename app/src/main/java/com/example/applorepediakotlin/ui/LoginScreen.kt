package com.example.applorepediakotlin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applorepediakotlin.model.UserCredentials
import com.example.applorepediakotlin.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    // ⭐ 1. AÑADIR PARÁMETRO DE NAVEGACIÓN A REGISTRO
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    // Nota: Asumiendo que has añadido loginError y isLoading a AuthViewModel
    val error by authViewModel.loginError.collectAsState(initial = null)
    val isLoading by authViewModel.isLoading.collectAsState(initial = false)

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            onLoginSuccess()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (Xano)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
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
                onClick = {
                    // ⭐ CORRECCIÓN CLAVE: Añadir el campo 'name' con un valor en blanco.
                    // Xano lo ignorará para el login, pero Kotlin lo requiere para compilar.
                    authViewModel.login(UserCredentials(email, password, name = ""))
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Ingresar")
                }
            }

            // 2. IMPLEMENTACIÓN DEL BOTÓN DE REGISTRO
            Spacer(Modifier.height(16.dp))
            TextButton(
                onClick = onNavigateToSignup,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}