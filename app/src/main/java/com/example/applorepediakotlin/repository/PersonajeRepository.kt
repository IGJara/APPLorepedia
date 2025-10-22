package com.example.applorepediakotlin.repository

import com.example.applorepediakotlin.model.Personaje

class PersonajeRepository {

    // Lista de datos simulados con los nuevos personajes
    private val personajesSimulados = listOf(
        Personaje(
            id = 1,
            nombre = "Gerson Boom",
            juego = "Undertale y Deltarune",
            descripcion = "Una tortuga anciana que vende objetos de coleccionista. Se le conocio como el 'El señor del martillo'."
        ),
        Personaje(
            id = 2,
            nombre = "Dante",
            juego = "Devil May Cry",
            descripcion = "Cazador de demonios, hijo del legendario demonio Sparda y la humana Eva. Es un antihéroe cínico pero honorable."
        ),
        Personaje(
            id = 3,
            nombre = "Spamton G. Spamton",
            juego = "Deltarune",
            descripcion = "Un títere defectuoso y vendedor de 'ofertas' que reside en el Ciber Mundo. ¡ES TU OPORTUNIDAD PARA SER UN PEZ GORDO!"
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