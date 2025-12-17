package com.example.applorepediakotlin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applorepediakotlin.viewmodel.AuthViewModel
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Signup : Screen("signup_screen")
    object Home : Screen("home_screen")
    object ListaPersonajes : Screen("lista_personajes")

    // ⭐ CAMBIO 1: Detalle ahora es la VISTA (lectura y música)
    object VistaPersonaje : Screen("vista_personaje/{personajeId}") {
        fun createRoute(personajeId: Int) = "vista_personaje/$personajeId"
    }

    // ⭐ CAMBIO 2: Nueva ruta para EDITAR
    object EditarPersonaje : Screen("editar_personaje/{personajeId}") {
        fun createRoute(personajeId: Int) = "editar_personaje/$personajeId"
    }

    object Evaluacion : Screen("evaluacion")
    object CrearPersonaje : Screen("crear_personaje")
}

@Composable
fun NavegacionApp(
    personajeViewModel: PersonajeViewModel,
    authViewModel: AuthViewModel
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState(initial = false)
    val navController = rememberNavController()

    val startDestination = if (isAuthenticated) Screen.Home.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {

        // 0. Login & Registro (Sin cambios)
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                authViewModel = authViewModel,
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 1. Home (Corregido para evitar errores de parámetros faltantes)
        composable(Screen.Home.route) {
            HomeScreen(
                personajeViewModel = personajeViewModel,
                authViewModel = authViewModel,
                onNavigateToLista = { navController.navigate(Screen.ListaPersonajes.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Listado
        composable(Screen.ListaPersonajes.route) {
            PersonajeListScreen(
                viewModel = personajeViewModel,
                onPersonajeClick = { personajeId ->
                    // ⭐ Al hacer click, vamos a la VISTA, no a la edición
                    navController.navigate(Screen.VistaPersonaje.createRoute(personajeId))
                },
                onNavigateToCrearPersonaje = { navController.navigate(Screen.CrearPersonaje.route) }
            )
        }

        // 3. ⭐ NUEVA: Vista de Ficha (PersonajeViewScreen)
        composable(
            route = Screen.VistaPersonaje.route,
            arguments = listOf(navArgument("personajeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("personajeId") ?: 0
            PersonajeViewScreen(
                viewModel = personajeViewModel,
                personajeId = id,
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { idToEdit ->
                    navController.navigate(Screen.EditarPersonaje.createRoute(idToEdit))
                }
            )
        }

        // 4. ⭐ NUEVA: Pantalla de Edición (PersonajeEditScreen)
        composable(
            route = Screen.EditarPersonaje.route,
            arguments = listOf(navArgument("personajeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("personajeId") ?: 0
            PersonajeEditScreen(
                viewModel = personajeViewModel,
                personajeId = id,
                onBack = { navController.popBackStack() }
            )
        }

        // 5. Evaluación & Creación
        composable(Screen.Evaluacion.route) {
            // EvaluacionScreen()
        }

        composable(Screen.CrearPersonaje.route) {
            CrearPersonajeScreen(
                viewModel = personajeViewModel,
                onBack = { navController.popBackStack() },
                onPersonajeGuardado = { navController.popBackStack() }
            )
        }
    }
}