package com.example.applorepediakotlin.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applorepediakotlin.R
import com.example.applorepediakotlin.viewmodel.EvaluacionViewModel
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

// Definición de las rutas de pantalla
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ListaPersonajes : Screen("lista_personajes")
    // Se mantiene el argumento de ruta para el ID del personaje
    object DetallePersonaje : Screen("detalle_personaje/{personajeId}") {
        fun createRoute(personajeId: Int) = "detalle_personaje/$personajeId"
    }
    // Nueva ruta para el formulario de evaluación
    object Evaluacion : Screen("evaluacion")
}

@Composable
fun AppNavigation(
    viewModel: PersonajeViewModel // El ViewModel principal se pasa desde MainActivity
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        // 1. Pantalla de Inicio
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                // Navega a la lista de personajes
                onNavigateToLista = { navController.navigate(Screen.ListaPersonajes.route) },
                // Nueva acción: Navega a la pantalla de evaluación
                onNavigateToEvaluacion = { navController.navigate(Screen.Evaluacion.route) }
            )
        }

        // 2. Pantalla de Lista de Personajes
        composable(Screen.ListaPersonajes.route) {
            // El título de la AppBar ahora usa stringResource
            PersonajeListScreen(
                viewModel = viewModel,
                onPersonajeClick = { personajeId ->
                    navController.navigate(Screen.DetallePersonaje.createRoute(personajeId))
                }
            )
        }

        // 3. Pantalla de Detalle de Personaje
        composable(Screen.DetallePersonaje.route) { backStackEntry ->
            val idString = backStackEntry.arguments?.getString("personajeId")
            val personajeId = idString?.toIntOrNull() ?: return@composable

            PersonajeDetailScreen(
                personajeId = personajeId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 4. Pantalla de Evaluación (Nueva)
        composable(Screen.Evaluacion.route) {
            // Inicializa el ViewModel específico de la evaluación
            val evaluacionViewModel: EvaluacionViewModel = viewModel()
            EvaluacionScreen(
                viewModel = evaluacionViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
