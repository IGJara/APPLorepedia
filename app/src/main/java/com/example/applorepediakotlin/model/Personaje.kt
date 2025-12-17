// Archivo: com.example.applorepediakotlin.model/Personaje.kt

package com.example.applorepediakotlin.model

data class Personaje(
    val id: Int,
    val nombre: String,
    val juego: String,
    val descripcion: String,
    val imagenUrl: String?,
    // Campo añadido en un paso anterior (mantenido)
    val musicaUrl: String? = null,
    val imagenResId: Int?,

    // userId añadido (mantenido)
    val userId: Int? = null
)

// Mapeo: De Domain a DTO (para enviar a Xano)
fun Personaje.toPersonajeDto(): PersonajeDto {
    return PersonajeDto(
        id = if (this.id == 0) null else this.id,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl,

        // Mapeo de musicaUrl al DTO (mantenido)
        musicaUrl = this.musicaUrl,

        // userId mapeado (mantenido)
        userId = this.userId
    )
}

// Mapeo: De Domain a Entity (para guardar en la base de datos local)
fun Personaje.toEntity(): PersonajeEntity {
    return PersonajeEntity(
        id = this.id,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl,

        // ⭐ CORRECCIÓN CLAVE: Mapear el nuevo campo 'musicaUrl' a la Entidad.
        musicaUrl = this.musicaUrl
        // Nota: PersonajeEntity no tiene userId, por lo que no se mapea aquí.
    )
}