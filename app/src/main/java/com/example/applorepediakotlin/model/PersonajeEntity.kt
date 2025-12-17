// com.example.applorepediakotlin.model/PersonajeEntity.kt

package com.example.applorepediakotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personajes")
data class PersonajeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val juego: String,
    val descripcion: String,
    val imagenUrl: String?,

    // ⭐ CORRECCIÓN CLAVE 1: Añadir el campo musicaUrl para sincronizar con Personaje.kt
    val musicaUrl: String?
)

// ⭐ Mapeo: De Entity a Domain (para usar en el Repositorio/ViewModel)
fun PersonajeEntity.toDomain(): Personaje {
    return Personaje(
        id = this.id,
        nombre = this.nombre,
        juego = this.juego,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl,

        // ⭐ CORRECCIÓN CLAVE 2: Mapear el nuevo campo
        musicaUrl = this.musicaUrl,

        imagenResId = null,
        // userId: Int? = null (Este campo está en Personaje.kt, pero si no está en PersonajeEntity.kt,
        // debe dejarse con su valor por defecto 'null' o añadirlo si es persistido localmente.)
        userId = null
    )
}
// NOTA: La función fun Personaje.toEntity() que se encuentra en Personaje.kt debe
// ser revisada para asegurar que también mapea 'musicaUrl' de Personaje a PersonajeEntity.