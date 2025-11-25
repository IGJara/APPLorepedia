package com.example.applorepediakotlin.api

import android.content.Context
import com.example.applorepediakotlin.model.Personaje
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.InputStreamReader

// Data class que mapea la estructura de la raíz de tu db.json
// Esto es importante para que Gson pueda leer correctamente
data class LocalPersonajesResponse(
    @SerializedName("personajes") // Asegura que mapea la clave "personajes"
    val personajes: List<Personaje>
)

object LocalDataSource {

    // Función que lee el JSON del archivo en assets
    fun loadPersonajesFromAssets(context: Context): List<Personaje> {
        return try {
            // Abre el archivo db.json
            val inputStream = context.assets.open("db.json")
            val reader = InputStreamReader(inputStream)

            // Usa Gson para convertir el contenido del archivo a la clase LocalPersonajesResponse
            val jsonResponse = Gson().fromJson(reader, LocalPersonajesResponse::class.java)
            reader.close()

            // Retorna la lista de personajes
            jsonResponse.personajes
        } catch (e: Exception) {
            // Si falla, imprime el error y retorna una lista vacía
            e.printStackTrace()
            emptyList()
        }
    }
}