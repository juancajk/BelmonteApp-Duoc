package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repositorio que abstrae el acceso a datos de viajes.
 * Responsabilidad Constanza: Capa intermedia entre ViewModel y Room DAO.
 *
 * Centraliza operaciones de Room y en el futuro podria
 * sincronizar con la API REST de Spring Boot (Parte 1 - Juan Carlos).
 *
 * Cumplimiento normativo: Todos los datos son ficticios/sinteticos
 * segun Ley 21.719 y Ley 19.628. No se usan datos reales.
 */
class ViajeRepository(private val viajeDao: ViajeDao) {

    // === Flujos reactivos (Flow) ===
    fun obtenerTodosLosViajes(): Flow<List<ViajeEntity>> =
        viajeDao.obtenerTodosLosViajes()

    fun obtenerViajesPorEstado(estado: String): Flow<List<ViajeEntity>> =
        viajeDao.obtenerViajesPorEstado(estado)

    fun obtenerViajesDelDia(fechaHoy: String): Flow<List<ViajeEntity>> =
        viajeDao.obtenerViajesDelDia(fechaHoy)

    // === Operaciones suspendidas ===
    suspend fun obtenerViajePorId(id: String): ViajeEntity? =
        viajeDao.obtenerViajePorId(id)

    suspend fun insertarViaje(viaje: ViajeEntity) =
        viajeDao.insertarViaje(viaje)

    suspend fun insertarViajes(viajes: List<ViajeEntity>) =
        viajeDao.insertarViajes(viajes)

    suspend fun actualizarViaje(viaje: ViajeEntity) =
        viajeDao.actualizarViaje(viaje)

    suspend fun actualizarEstado(viajeId: String, nuevoEstado: String) =
        viajeDao.actualizarEstado(viajeId, nuevoEstado)

    suspend fun eliminarTodos() =
        viajeDao.eliminarTodos()

    suspend fun contarTotal(): Int =
        viajeDao.contarTotal()

    /**
     * Carga datos ficticios de prueba para demostracion del MVP.
     * Solo inserta si la base de datos esta vacia (evita duplicados).
     *
     * NOTA: Cumple con el requisito obligatorio del curso DSY1105:
     * "El desafio se desarrollara con datos ficticios, sinteticos,
     * agregados, o publicos debidamente anonimizados."
     * Todos los nombres, direcciones y vuelos son inventados.
     */
    suspend fun cargarDatosFicticios() {
        // Solo cargar si la BD esta vacia
        val total = viajeDao.contarTotal()
        if (total > 0) return

        val viajesFicticios = listOf(
            ViajeEntity(
                id = "VJ-001",
                nombrePasajero = "Carlos Mendoza",
                fecha = "10/10/2026",
                hora = "08:30",
                direccionOrigen = "Aeropuerto SCL, Santiago",
                direccionDestino = "Hotel Marriott, Av. Kennedy 5741",
                latitudOrigen = -33.3928,
                longitudOrigen = -70.7856,
                latitudDestino = -33.3994,
                longitudDestino = -70.5788,
                numeroVuelo = "LA-402",
                estado = "Pendiente"
            ),
            ViajeEntity(
                id = "VJ-002",
                nombrePasajero = "Ana Torres",
                fecha = "10/10/2026",
                hora = "09:15",
                direccionOrigen = "Oficina Central, Av. Providencia 1208",
                direccionDestino = "Aeropuerto SCL, Santiago",
                latitudOrigen = -33.4264,
                longitudOrigen = -70.6133,
                latitudDestino = -33.3928,
                longitudDestino = -70.7856,
                estado = "Asignado"
            ),
            ViajeEntity(
                id = "VJ-003",
                nombrePasajero = "Roberto Fuentes",
                fecha = "10/10/2026",
                hora = "11:00",
                direccionOrigen = "Hotel W, Isidora Goyenechea 3000",
                direccionDestino = "Centro de Convenciones, Av. Apoquindo 4500",
                latitudOrigen = -33.4167,
                longitudOrigen = -70.6000,
                latitudDestino = -33.4100,
                longitudDestino = -70.5900,
                estado = "En Curso"
            ),
            ViajeEntity(
                id = "VJ-004",
                nombrePasajero = "Maria Gonzalez",
                fecha = "10/10/2026",
                hora = "07:00",
                direccionOrigen = "Residencia, Las Condes 12050",
                direccionDestino = "Aeropuerto SCL, Terminal Internacional",
                latitudOrigen = -33.4050,
                longitudOrigen = -70.5700,
                latitudDestino = -33.3928,
                longitudDestino = -70.7856,
                numeroVuelo = "IB-6834",
                estado = "Finalizado"
            ),
            ViajeEntity(
                id = "VJ-005",
                nombrePasajero = "Diego Ramirez",
                fecha = "10/10/2026",
                hora = "14:30",
                direccionOrigen = "Aeropuerto SCL, Terminal Nacional",
                direccionDestino = "Hotel Ritz, Av. El Golf 99",
                latitudOrigen = -33.3928,
                longitudOrigen = -70.7856,
                latitudDestino = -33.4130,
                longitudDestino = -70.5950,
                numeroVuelo = "LA-215",
                estado = "Pendiente"
            )
        )
        viajeDao.insertarViajes(viajesFicticios)
    }
}
