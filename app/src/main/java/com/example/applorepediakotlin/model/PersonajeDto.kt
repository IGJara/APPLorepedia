// com.example.applorepediakotlin.model/PersonajeDto.kt

package com.example.applorepediakotlin.model

import com.google.gson.annotations.SerializedName

data class PersonajeDto(
    val id: Int,
    val nombre: String,
    val juego: String,
    val descripcion: String,
    // El nombre del campo en el JSON de la API
    @SerializedName("url_imagen")
    val imagenUrl: String?
)

// Función de Mapeo: De DTO (API) a Entity (BD)

fun PersonajeDto.toEntity(): PersonajeEntity {
    return PersonajeEntity(
        id = this.id,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl
    )
}