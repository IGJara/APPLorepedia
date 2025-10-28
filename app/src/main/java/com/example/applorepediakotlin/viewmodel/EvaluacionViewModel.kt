package com.example.applorepediakotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class EvaluacionViewModel : ViewModel() {
    // Estado del formulario
    var nombreEvaluador by mutableStateOf("")
        private set
    var comentario by mutableStateOf("")
        private set
    var calificacionDiseno by mutableStateOf(5f) // Calificación por defecto 5/10
        private set
    var calificacionFuncionalidad by mutableStateOf(5f)
        private set

    // Estado para confirmar el envío (mensaje al usuario)
    var isSubmitted by mutableStateOf(false)
        private set

    // Función de actualización de los campos
    fun updateNombre(newNombre: String) { nombreEvaluador = newNombre }
    fun updateComentario(newComentario: String) { comentario = newComentario }
    fun updateDiseno(newRating: Float) { calificacionDiseno = newRating }
    fun updateFuncionalidad(newRating: Float) { calificacionFuncionalidad = newRating }

    // Función que simula el envío del formulario
    fun submitFormulario() {
        // Aquí se simularía guardar los datos en una base de datos o en un repositorio.
        val promedio = (calificacionDiseno + calificacionFuncionalidad) / 2

        println("--- Formulario Enviado ---")
        println("Evaluador: $nombreEvaluador")
        println("Comentario: $comentario")
        println("Calificación Diseño: $calificacionDiseno / 10")
        println("Calificación Funcionalidad: $calificacionFuncionalidad / 10")
        println("Promedio: $promedio")
        println("--------------------------")

        // Muestra el mensaje de éxito
        isSubmitted = true
        // Opcional: reiniciar campos después de 3 segundos
        // Nota: para simplificar, no lo reiniciaremos aquí.
    }

    // Función para reiniciar el estado de envío y permitir otro intento
    fun resetSubmissionStatus() {
        isSubmitted = false
        nombreEvaluador = ""
        comentario = ""
        calificacionDiseno = 5f
        calificacionFuncionalidad = 5f
    }
}
