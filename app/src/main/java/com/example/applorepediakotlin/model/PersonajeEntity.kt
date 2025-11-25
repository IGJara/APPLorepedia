// com.example.applorepediakotlin.model/PersonajeEntity.kt (MODIFICADO)

package com.example.applorepediakotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personajes")
data class PersonajeEntity(
    // ⭐ CAMBIO CLAVE: Habilitar la autogeneración de ID y asignar un valor por defecto
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val juego: String,
    val descripcion: String,
    // Guardamos la URL de la imagen, ya que viene de la API o es una URI local
    val imagenUrl: String?
)

// Funciones de Mapeo (Mappers)

fun PersonajeEntity.toDomain(): Personaje {
    return Personaje(
        id = this.id,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl,
        imagenResId = null
    )
}

fun Personaje.toEntity(): PersonajeEntity {
    return PersonajeEntity(
        // Al convertir de dominio a entidad, pasamos el ID,
        // que será 0 para personajes nuevos (autogenerado) o >0 para existentes.
        id = this.id,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl
    )
}