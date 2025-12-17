package com.example.applorepediakotlin.api

import com.example.applorepediakotlin.model.PersonajeDto
import retrofit2.http.*

interface PersonajeService {

    // ⭐ CORRECCIÓN CLAVE: Usar solo la ruta interna '/character'.
    // El slug de Xano (h-qL_XBL) debe estar en la URL base en RetrofitClient.kt
    @GET("character")
    suspend fun getPersonajes(): List<PersonajeDto>

    @GET("character/{character_id}") // Endpoint GET por ID
    suspend fun getPersonajeById(@Path("character_id") id: Int): PersonajeDto

    @POST("character")
    suspend fun createPersonaje(@Body personaje: PersonajeDto): PersonajeDto

    // CORRECCIÓN: Usando PATCH (más acorde a Xano para 'Edit character record')
    @PATCH("character/{character_id}")
    suspend fun updatePersonaje(
        @Path("character_id") id: Int,
        @Body personaje: PersonajeDto
    ): PersonajeDto

    @DELETE("character/{character_id}")
    suspend fun deletePersonaje(@Path("character_id") id: Int)
}