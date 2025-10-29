package com.example.applorepediakotlin.model

import androidx.annotation.DrawableRes

data class Personaje(
    val id: Int,
    val nombre: String,
    val juego: String,
    val descripcion: String,
    // CAMBIO: Ahora usa un Int para el ID del recurso local (R.drawable.xxx)
    @DrawableRes val imagenResId: Int? = null
)