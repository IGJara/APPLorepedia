package com.example.applorepediakotlin.ui

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AudioPlayerManager(private val context: Context) : DefaultLifecycleObserver {

    private var mediaPlayer: MediaPlayer? = null

    // Estado para que la UI de Compose reaccione a cambios de reproducción
    var isAudioPlaying by mutableStateOf(false)
        private set

    fun play(url: String) {
        if (url.isBlank()) return

        try {
            stop() // Limpiar instancia anterior

            mediaPlayer = MediaPlayer().apply {
                // Se usa el contexto y el Uri parseado para evitar errores de permisos básicos
                setDataSource(context, Uri.parse(url))

                setOnPreparedListener {
                    it.start()
                    isAudioPlaying = true
                }

                setOnCompletionListener {
                    isAudioPlaying = false
                }

                setOnErrorListener { _, _, _ ->
                    isAudioPlaying = false
                    true
                }

                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isAudioPlaying = false
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
            isAudioPlaying = false
        }
    }

    fun togglePlayback(url: String) {
        if (isAudioPlaying) {
            stop()
        } else {
            play(url)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        stop()
    }
}