package com.example.applorepediakotlin.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applorepediakotlin.viewmodel.PersonajeViewModel



// Rutas de navegación
sealed class Screen(val route: String) {
    data object Lista : Screen("lista")
    data object Detalle : Screen("detalle/{personajeId}") {
        fun createRoute(personajeId: Int) = "detalle/$personajeId"
    }
}

@Composable
fun AppNavigation(viewModel: PersonajeViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Lista.route
    ) {
        // Pantalla de Lista
        composable(Screen.Lista.route) {
            PersonajeListScreen(
                viewModel = viewModel,
                onPersonajeClick = { personajeId ->
                    navController.navigate(Screen.Detalle.createRoute(personajeId))
                }
            )
        }

        // Pantalla de Detalle
        composable(
            route = Screen.Detalle.route,
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