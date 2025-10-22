package com.example.applorepediakotlin.model

data class Personaje(
    val id: Int,
    val nombre: String,
    val juego: String,
    val descripcion: String,
    val imagenUrl: String? = null // Para la galería o una URL simulada
)