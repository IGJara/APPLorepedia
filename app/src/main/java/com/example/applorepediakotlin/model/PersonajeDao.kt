// com.example.applorepediakotlin.model/PersonajeDao.kt

package com.example.applorepediakotlin.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
// ⭐ NUEVA IMPORTACIÓN
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonajeDao {
    // Retorna todos los personajes como un Flow para observarlos
    @Query("SELECT * FROM personajes ORDER BY nombre ASC")
    fun getAllPersonajes(): Flow<List<PersonajeEntity>>

    // Inserta una lista de personajes (usado para la carga inicial)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(personajes: List<PersonajeEntity>)

    // Inserta o actualiza un solo personaje
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonaje(personaje: PersonajeEntity)

    // Obtiene un personaje por ID
    @Query("SELECT * FROM personajes WHERE id = :id")
    fun getPersonajeById(id: Int): Flow<PersonajeEntity?>

    // ⭐ NUEVA FUNCIÓN: Elimina un personaje de la base de datos
    @Delete
    suspend fun deletePersonaje(personaje: PersonajeEntity)
}