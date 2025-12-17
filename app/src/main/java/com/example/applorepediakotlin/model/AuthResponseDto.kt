package com.example.applorepediakotlin.model

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("authToken")
    val token: String,

    @SerializedName("user_id")
    val userId: Int?,

    // ⭐ CORRECCIÓN CLAVE: ASUMIENDO que Xano ahora devolverá el nombre en el campo 'name'
    @SerializedName("name")
    val name: String? // El nombre del usuario logueado
)