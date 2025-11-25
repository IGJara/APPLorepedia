// com.example.applorepediakotlin.ui/Navegacion.kt

package com.example.applorepediakotlin.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applorepediakotlin.viewmodel.EvaluacionViewModel
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel

// Definición de las rutas de pantalla
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ListaPersonajes : Screen("lista_personajes")
    object DetallePersonaje : Screen("detalle_personaje/{personajeId}") {
        fun createRoute(personajeId: Int) = "detalle_personaje/$personajeId"
    }
    object Evaluacion : Screen("evaluacion")
    object CrearPersonaje : Screen("crear_personaje")
}

@Composable
fun AppNavigation(
    // El ViewModel se recibe como argumento aquí
    viewModel: PersonajeViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        // 1. Pantalla de Inicio
        composable(Screen.Home.route) {
            HomeScreen(
                // Aquí lo pasas correctamente
                viewModel = viewModel,
                onNavigateToLista = { navController.navigate(Screen.ListaPersonajes.route) },
                onNavigateToEvaluacion = { navController.navigate(Screen.Evaluacion.route) },
                onNavigateToCrearPersonaje = { navController.navigate(Screen.CrearPersonaje.route) }
            )
        }

        // 2. Pantalla de Listado de Personajes
        composable(Screen.ListaPersonajes.route) {
            PersonajeListScreen(
                viewModel = viewModel,
                onPersonajeClick = { personajeId ->
                    navController.navigate(Screen.DetallePersonaje.createRoute(personajeId))
                }
            )
        }

        // 3. Pantalla de Detalle de Personaje (con argumento)
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

        // 4. Pantalla de Evaluación
        composable(Screen.Evaluacion.route) {
            val evaluacionViewModel = viewModel<EvaluacionViewModel>()
            EvaluacionScreen(
                viewModel = evaluacionViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 5. Pantalla de Creación de Personaje
        composable(Screen.CrearPersonaje.route) {
            CrearPersonajeScreen(
                // ⭐ CORRECCIÓN CLAVE: Pasamos el objeto 'viewModel' sin paréntesis
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPersonajeGuardado = { navController.popBackStack() }
            )
        }
    }
}