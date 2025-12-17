package com.example.applorepediakotlin.api

import com.example.applorepediakotlin.datastore.AuthDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// ⭐ CRÍTICO: Definimos AMBAS URLs para que AppLorepediaApplication pueda elegir.
// Usaremos la URL de PERSONAJES como la BASE URL PRINCIPAL para este archivo (ya que Auth ya funciona).

// URL de los Endpoints de Personajes (Game Characters API)
const val PERSONAJE_BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:h-qL_XBL/"

// Si necesitas un servicio de autenticación separado:
const val AUTH_BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:ZsZNh16G/"


// Interceptor que añade el token de autenticación (Bearer) a todas las peticiones (Mantenido)
class AuthInterceptor(private val authDataStore: AuthDataStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token: String? = runBlocking {
            authDataStore.authToken.first()
        }

        val request = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(request)
    }
}

// Cliente OkHttpClient configurado para usar el Interceptor (Mantenido)
fun createOkHttpClient(authDataStore: AuthDataStore): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(authDataStore))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

// ⭐ MODIFICACIÓN: Función para crear la instancia de Retrofit (aceptando la URL como parámetro)
fun createRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl) // Usa la URL proporcionada
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

// Función genérica para crear los servicios (Mantenido)
inline fun <reified T> createService(retrofit: Retrofit): T = retrofit.create(T::class.java)