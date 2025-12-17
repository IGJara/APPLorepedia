package com.example.applorepediakotlin.model

import com.google.gson.annotations.SerializedName

data class UserCredentials(
    val email: String,
    val password: String,

    // ⭐ MODIFICACIÓN CRÍTICA: Se añade el campo 'name'.
    // Xano requiere este campo para el signup, y Retrofit lo enviará como 'name' en el JSON.
    @SerializedName("name")
    val name: String
)