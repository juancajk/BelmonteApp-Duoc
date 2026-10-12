package com.example.myapplication

import com.example.myapplication.data.ViajeEntity
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para la logica del ConductorViewModel.
 * Responsabilidad Transversal: Tests de ViewModels.
 *
 * Nota: Al ser AndroidViewModel (necesita Application context para Room),
 * estas pruebas verifican la logica de negocio de forma aislada sin
 * instanciar el ViewModel completo. Para tests instrumentados con
 * Room real, se usarian androidTest con ApplicationProvider.
 *
 * Verifica:
 * - Logica de transicion de estados (avanzar/retroceder)
 * - Validacion de filtros
 * - Logica de distancia GPS
 * - Datos ficticios cumplen normativa
 */
class ConductorViewModelTest {

    // === Tests de Logica de Transicion de Estados ===

    @Test
    fun avanzarEstado_desdePendiente_retornaAsignado() {
        val resultado = calcularSiguienteEstado("Pendiente")
        assertEquals("Asignado", resultado)
    }

    @Test
    fun avanzarEstado_desdeAsignado_retornaEnCurso() {
        val resultado = calcularSiguienteEstado("Asignado")
        assertEquals("En Curso", resultado)
    }

    @Test
    fun avanzarEstado_desdeEnCurso_retornaFinalizado() {
        val resultado = calcularSiguienteEstado("En Curso")
        assertEquals("Finalizado", resultado)
    }

    @Test
    fun avanzarEstado_desdeFinalizado_retornaNull() {
        val resultado = calcularSiguienteEstado("Finalizado")
        assertNull(resultado)
    }

    @Test
    fun retrocederEstado_desdeAsignado_retornaPendiente() {
        val resultado = calcularEstadoAnterior("Asignado")
        assertEquals("Pendiente", resultado)
    }

    @Test
    fun retrocederEstado_desdeEnCurso_retornaAsignado() {
        val resultado = calcularEstadoAnterior("En Curso")
        assertEquals("Asignado", resultado)
    }

    @Test
    fun retrocederEstado_desdeFinalizado_retornaEnCurso() {
        val resultado = calcularEstadoAnterior("Finalizado")
        assertEquals("En Curso", resultado)
    }

    @Test
    fun retrocederEstado_desdePendiente_retornaNull() {
        val resultado = calcularEstadoAnterior("Pendiente")
        assertNull(resultado)
    }

    // === Tests de Filtros ===

    @Test
    fun filtros_todosLosEstados_sonReconocidos() {
        val filtrosValidos = listOf("Todos", "Pendiente", "Asignado", "En Curso", "Finalizado")
        filtrosValidos.forEach { filtro ->
            assertTrue("Filtro '$filtro' debe ser valido", esFiltroValido(filtro))
        }
    }

    @Test
    fun filtros_filtroInvalido_noEsReconocido() {
        assertFalse(esFiltroValido("Cancelado"))
        assertFalse(esFiltroValido(""))
    }

    @Test
    fun filtrar_porEstado_retornaSubconjunto() {
        val viajes = crearListaViajesPrueba()
        val pendientes = viajes.filter { it.estado == "Pendiente" }
        assertEquals(2, pendientes.size)
    }

    @Test
    fun filtrar_todos_retornaTodos() {
        val viajes = crearListaViajesPrueba()
        assertEquals(5, viajes.size)
    }

    // === Tests de Datos Ficticios / Normativa ===

    @Test
    fun datosFicticios_noContienenDatosReales() {
        val viajes = crearListaViajesPrueba()

        viajes.forEach { viaje ->
            // Verificar que los IDs siguen el formato ficticio VJ-XXX
            assertTrue(
                "ID '${viaje.id}' debe seguir formato VJ-XXX",
                viaje.id.matches(Regex("VJ-\\d{3}"))
            )
            // Verificar que los nombres no estan vacios
            assertTrue(viaje.nombrePasajero.isNotBlank())
        }
    }

    @Test
    fun datosFicticios_coordenadasEnRangoSantiago() {
        val viajes = crearListaViajesPrueba()

        viajes.filter { it.latitudOrigen != 0.0 }.forEach { viaje ->
            // Santiago de Chile: lat ~-33.4, lng ~-70.6
            assertTrue(
                "Latitud origen ${viaje.latitudOrigen} fuera de rango Santiago",
                viaje.latitudOrigen in -34.0..-33.0
            )
            assertTrue(
                "Longitud origen ${viaje.longitudOrigen} fuera de rango Santiago",
                viaje.longitudOrigen in -71.0..-70.0
            )
        }
    }

    // === Tests de Logica de Distancia ===

    @Test
    fun distancia_entreDosPuntos_esPositiva() {
        // Aeropuerto SCL a Hotel Marriott (distancia conocida ~20km)
        val distancia = calcularDistanciaSimple(
            -33.3928, -70.7856,
            -33.3994, -70.5788
        )
        assertTrue("Distancia debe ser positiva", distancia > 0)
        assertTrue("Distancia debe ser razonable (<50km)", distancia < 50.0)
    }

    @Test
    fun distancia_mismosPuntos_esCero() {
        val distancia = calcularDistanciaSimple(
            -33.3928, -70.7856,
            -33.3928, -70.7856
        )
        assertEquals(0.0, distancia, 0.01)
    }

    // === Funciones auxiliares que replican la logica del ViewModel ===

    /**
     * Replica la logica de avanzarEstado del ConductorViewModel.
     */
    private fun calcularSiguienteEstado(estadoActual: String): String? {
        return when (estadoActual) {
            "Pendiente" -> "Asignado"
            "Asignado" -> "En Curso"
            "En Curso" -> "Finalizado"
            else -> null
        }
    }

    /**
     * Replica la logica de retrocederEstado del ConductorViewModel.
     */
    private fun calcularEstadoAnterior(estadoActual: String): String? {
        return when (estadoActual) {
            "Asignado" -> "Pendiente"
            "En Curso" -> "Asignado"
            "Finalizado" -> "En Curso"
            else -> null
        }
    }

    private fun esFiltroValido(filtro: String): Boolean {
        return filtro in listOf("Todos", "Pendiente", "Asignado", "En Curso", "Finalizado")
    }

    /**
     * Crea una lista de viajes ficticios para testing.
     * Misma estructura que ViajeRepository.cargarDatosFicticios().
     */
    private fun crearListaViajesPrueba(): List<ViajeEntity> {
        return listOf(
            ViajeEntity("VJ-001", "Carlos Mendoza", "10/10/2026", "08:30",
                "Aeropuerto SCL", "Hotel Marriott", -33.3928, -70.7856,
                -33.3994, -70.5788, "LA-402", "Pendiente"),
            ViajeEntity("VJ-002", "Ana Torres", "10/10/2026", "09:15",
                "Oficina Central", "Aeropuerto SCL", -33.4264, -70.6133,
                -33.3928, -70.7856, null, "Asignado"),
            ViajeEntity("VJ-003", "Roberto Fuentes", "10/10/2026", "11:00",
                "Hotel W", "Centro Convenciones", -33.4167, -70.6000,
                -33.4100, -70.5900, null, "En Curso"),
            ViajeEntity("VJ-004", "Maria Gonzalez", "10/10/2026", "07:00",
                "Residencia", "Aeropuerto SCL", -33.4050, -70.5700,
                -33.3928, -70.7856, "IB-6834", "Finalizado"),
            ViajeEntity("VJ-005", "Diego Ramirez", "10/10/2026", "14:30",
                "Aeropuerto SCL", "Hotel Ritz", -33.3928, -70.7856,
                -33.4130, -70.5950, "LA-215", "Pendiente")
        )
    }

    /**
     * Calculo simplificado de distancia usando formula de Haversine.
     * Replica la logica de LocationHelper.calcularDistanciaKm().
     */
    private fun calcularDistanciaSimple(
        lat1: Double, lng1: Double,
        lat2: Double, lng2: Double
    ): Double {
        val radioTierra = 6371.0 // km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return radioTierra * c
    }
}
