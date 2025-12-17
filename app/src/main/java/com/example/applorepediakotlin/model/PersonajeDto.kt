// com.example.applorepediakotlin.model/PersonajeDto.kt

package com.example.applorepediakotlin.model

import com.google.gson.annotations.SerializedName

data class PersonajeDto(
    // CORRECCIONES DE API (Mantenidas)
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val nombre: String,

    @SerializedName("game")
    val juego: String,

    @SerializedName("description") // Mapeo de Xano 'description' a Kotlin 'descripcion'
    val descripcion: String,

    @SerializedName("image_url")
    val imagenUrl: String?,

    // Campo añadido en pasos anteriores (Mantenido)
    @SerializedName("music_url")
    val musicaUrl: String?,

    @SerializedName("user_id")
    val userId: Int?
)

// ⭐ Mapeo: De DTO a Domain (FUNCIÓN CORREGIDA)
fun PersonajeDto.toDomain(): Personaje {
    return Personaje(
        id = this.id ?: 0,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl,

        // ⭐ CORRECCIÓN CLAVE: Mapear el campo 'musicaUrl' de DTO al Domain.
        musicaUrl = this.musicaUrl,

        imagenResId = null,
        userId = this.userId
    )
}

// Mapeo a Entity (Mantenido)
fun PersonajeDto.toEntity(): PersonajeEntity {
    return PersonajeEntity(
        id = this.id ?: 0,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl,
        // Si PersonajeEntity tiene musicaUrl, DEBE ser añadido aquí
        musicaUrl = this.musicaUrl
    )
}