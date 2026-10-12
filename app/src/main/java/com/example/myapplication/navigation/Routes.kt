package com.example.myapplication.navigation

/**
 * Rutas de navegacion de la aplicacion Belmonte SpA.
 * Responsabilidad compartida (Juan Carlos + Constanza).
 */
object Routes {
    // === Flujo Inicial (compartido) ===
    val SPLASH = "splash"
    val ONBOARDING = "onboarding"
    val SELECCION_ROL = "seleccion_rol"  // NUEVO: Pantalla de seleccion Pasajero/Conductor

    // === Flujo Pasajero (Juan Carlos - Parte 1) ===
    val SOLICITUD = "solicitud"

    // === Flujo Conductor (Constanza - Parte 2) ===
    val LOGIN_CONDUCTOR = "login_conductor"
    val RUTAS_CONDUCTOR = "rutas_conductor"

    // === Generales (Plantilla Profesor) ===
    val HOME = "home"
    val LOGIN = "login"
}
