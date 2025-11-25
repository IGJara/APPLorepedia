// com.example.applorepediakotlin.model/Personaje.kt

package com.example.applorepediakotlin.model

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName // ⭐ IMPORTACIÓN NECESARIA

data class Personaje(
    val id: Int,
    val nombre: String,
    val juego: String,
    val descripcion: String,

    // ⭐ CORRECCIÓN: Usamos SerializedName para mapear 'url_imagen' del JSON
    @SerializedName("url_imagen")
    val imagenUrl: String? = null,

    @DrawableRes val imagenResId: Int? = null
)