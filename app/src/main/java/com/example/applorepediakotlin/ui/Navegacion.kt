package com.example.applorepediakotlin.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

// Rutas de Navegación
sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object ListaPersonajes : Screen("personaje_list_screen")
    // --- NUEVA RUTA DE EVALUACIÓN ---
    object Evaluacion : Screen("evaluacion_screen")
    // ---------------------------------
    object DetallePersonaje : Screen("personaje_detail_screen/{personajeId}") {
        fun createRoute(personajeId: Int) = "personaje_detail_screen/$personajeId"
    }
}

@Composable
fun AppNavigation(viewModel: PersonajeViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // --- 1. HOME SCREEN ---
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToLista = {
                    navController.navigate(Screen.ListaPersonajes.route)
                },
                // --- NUEVA NAVEGACIÓN A FORMULARIO ---
                onNavigateToEvaluacion = {
                    navController.navigate(Screen.Evaluacion.route)
                }
            )
        }

        // --- 2. LISTA DE PERSONAJES ---
        composable(Screen.ListaPersonajes.route) {
            PersonajeListScreen(
                viewModel = viewModel,
                onPersonajeClick = { personajeId ->
                    navController.navigate(Screen.DetallePersonaje.createRoute(personajeId))
                }
            )
        }

        // --- 3. PANTALLA DE EVALUACIÓN (NUEVA) ---
        composable(Screen.Evaluacion.route) {
            // EvaluacionScreen utiliza su propio ViewModel (EvaluacionViewModel)
            EvaluacionScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // --- 4. DETALLE DEL PERSONAJE ---
        composable(
            route = Screen.DetallePersonaje.route,
            arguments = listOf(navArgument("personajeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("personajeId") ?: 0
            PersonajeDetailScreen(
                viewModel = viewModel,
                personajeId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
