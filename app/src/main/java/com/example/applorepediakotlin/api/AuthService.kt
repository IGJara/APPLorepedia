package com.example.applorepediakotlin.api

import com.example.applorepediakotlin.model.AuthResponseDto
import com.example.applorepediakotlin.model.UserCredentials
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    // Login: POST /auth/login
    @POST("auth/login")
    suspend fun login(
        @Body credentials: UserCredentials
    ): AuthResponseDto

    // Logout: POST /auth/logout
    @POST("auth/logout")
    suspend fun logout()

    // ⭐ CORRECCIÓN: Renombrada a 'signup' para mayor claridad y consistencia.
    // Signup: POST /auth/signup
    @POST("auth/signup")
    suspend fun signup(@Body credentials: UserCredentials): AuthResponseDto
}