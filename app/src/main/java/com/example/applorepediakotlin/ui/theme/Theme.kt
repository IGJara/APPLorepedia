package com.example.applorepediakotlin.ui.theme

import android.app.Activity
// ⭐ YA NO USAREMOS isSystemInDarkTheme() POR DEFECTO, pero se puede mantener la importación
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Nota: Asumiendo que Purple80, PurpleGrey80, Pink80, Purple40, etc., están definidos en Color.kt
// Si no están definidos, debes definirlos.

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
    // ...
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    // ...
)

@Composable
fun AppLorepediaKotlinTheme(
    // ⭐ CAMBIO CLAVE: Recibir explícitamente el estado del tema desde el ViewModel
    darkTheme: Boolean,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // En una aplicación real con Compose, se usa darkTheme sin más lógica de DynamicColor si no se implementa
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()

            // ⭐ AJUSTAR: El color de los iconos de la barra de estado debe ser CLARO (LightStatusBars=false)
            // cuando el tema es OSCURO (darkTheme=true) para que se vean sobre un fondo oscuro,
            // y OSCURO (LightStatusBars=true) cuando el tema es CLARO (darkTheme=false).
            // La lógica es: isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asumiendo que Typography está importada desde Type.kt
        content = content
    )
}