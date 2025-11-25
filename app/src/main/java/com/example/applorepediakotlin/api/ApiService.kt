// com.example.applorepediakotlin.api/ApiService.kt

package com.example.applorepediakotlin.api

import com.example.applorepediakotlin.model.PersonajeDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Lista de personajes
    @GET("personajes") // Cambia "personajes" al endpoint real de tu API
    suspend fun getPersonajesRemote(): Response<List<PersonajeDto>>

    // Detalle de un personaje
    @GET("personajes/{id}")
    suspend fun getPersonajeDetailRemote(@Path("id") id: Int): Response<PersonajeDto>
}