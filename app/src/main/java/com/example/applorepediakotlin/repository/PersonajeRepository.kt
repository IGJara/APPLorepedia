package com.example.applorepediakotlin.repository

// Importamos la clase R de nuestro proyecto para acceder a los recursos (drawables)
import com.example.applorepediakotlin.R
import com.example.applorepediakotlin.model.Personaje

class PersonajeRepository {

    // Lista de datos simulados con los nuevos personajes y sus IDs de imagen
    private val personajesSimulados = listOf(
        Personaje(
            id = 1,
            nombre = "Gerson Boom",
            juego = "Undertale y Deltarune",
            descripcion = "Una tortuga anciana que vende objetos de coleccionista. Se le conocio como el 'El señor del martillo'.",
            // ASIGNACIÓN DE IMAGEN: Usamos el ID del recurso local (R.drawable.gerson_boom)
            imagenResId = R.drawable.gerson_boom
        ),
        Personaje(
            id = 2,
            nombre = "Dante",
            juego = "Devil May Cry",
            descripcion = "Cazador de demonios, hijo del legendario demonio Sparda y la humana Eva. Es un antihéroe cínico pero honorable.",
            // ASIGNACIÓN DE IMAGEN: Usamos el ID del recurso local (R.drawable.dante_sparta)
            imagenResId = R.drawable.dante_sparta
        ),
        Personaje(
            id = 3,
            nombre = "Spamton G. Spamton",
            juego = "Deltarune",
            descripcion = "Un títere defectuoso y vendedor de 'ofertas' que reside en el Ciber Mundo. ¡ES TU OPORTUNIDAD PARA SER UN PEZ GORDO!",
            // ASIGNACIÓN DE IMAGEN: Usamos el ID del recurso local (R.drawable.spamtom_g_spamtom)
            imagenResId = R.drawable.spamtom_g_spamtom
        )
    )

    // Función para obtener todos los personajes
    fun obtenerTodosLosPersonajes(): List<Personaje> {
        return personajesSimulados
    }

    // Función para obtener un personaje por ID
    fun obtenerPersonaje(personajeId: Int): Personaje? {
        return personajesSimulados.find { it.id == personajeId }
    }
}