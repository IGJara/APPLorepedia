// build.gradle.kts (Project: Raíz)

plugins {
    // 1. Android Application Plugin
    id("com.android.application") version "8.2.0" apply false

    // 2. Kotlin Android Plugin (Suele ser suficiente para Compose)
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false

    // 3. Plugin KSP (Se mantiene)
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false

    // El plugin "org.jetbrains.kotlin.plugin.compose" se omite aquí.
    // Compose se activa con el plugin 'kotlin-android' y la configuración 'buildFeatures' en el módulo 'app'.
}