package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

class ViajeRepository(
    private val viajeDao: ViajeDao
) {
    fun obtenerTodosLosViajes(): Flow<List<ViajeEntity>> =
        viajeDao.obtenerTodosLosViajes()

    fun obtenerViajesPorEstado(estado: String): Flow<List<ViajeEntity>> =
        viajeDao.obtenerViajesPorEstado(estado)

    fun obtenerViajesDelDia(fechaHoy: String): Flow<List<ViajeEntity>> =
        viajeDao.obtenerViajesDelDia(fechaHoy)

    fun obtenerViajesPendientes(): Flow<List<ViajeEntity>> =
        viajeDao.obtenerViajesPendientes()

    suspend fun obtenerViajePorId(id: String): ViajeEntity? =
        viajeDao.obtenerViajePorId(id)

    suspend fun insertarViaje(viaje: ViajeEntity) {
        viajeDao.insertarViaje(viaje)
    }

    suspend fun insertarViajes(viajes: List<ViajeEntity>) =
        viajeDao.insertarViajes(viajes)

    suspend fun actualizarViaje(viaje: ViajeEntity) =
        viajeDao.actualizarViaje(viaje)

    suspend fun eliminarViaje(viaje: ViajeEntity) =
        viajeDao.eliminarViaje(viaje)

    suspend fun actualizarEstado(viajeId: String, nuevoEstado: String): Boolean {
        viajeDao.actualizarEstado(viajeId, nuevoEstado)
        return true
    }

    suspend fun asignarViaje(viajeId: String) =
        viajeDao.asignarViaje(viajeId)

    suspend fun sincronizarDesdeApi(): Boolean {
        return false
    }

    suspend fun eliminarTodos() =
        viajeDao.eliminarTodos()

    suspend fun contarTotal(): Int =
        viajeDao.contarTotal()

    suspend fun contarPorEstado(estado: String): Int =
        viajeDao.contarPorEstado(estado)

    suspend fun cargarDatosFicticios() {
        val total = viajeDao.contarTotal()
        if (total > 0) return

        val viajesFicticios = listOf(
            ViajeEntity(
                id = "VJ-001",
                nombrePasajero = "Carlos Mendoza",
                fecha = "12/10/2026",
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
                id = "VJ-005",
                nombrePasajero = "Diego Ramirez",
                fecha = "12/10/2026",
                hora = "14:30",
                direccionOrigen = "Aeropuerto SCL, Terminal Nacional",
                direccionDestino = "Hotel Ritz, Av. El Golf 99",
                latitudOrigen = -33.3928,
                longitudOrigen = -70.7856,
                latitudDestino = -33.4130,
                longitudDestino = -70.5950,
                numeroVuelo = "LA-215",
                estado = "Pendiente"
            ),
            ViajeEntity(
                id = "VJ-008",
                nombrePasajero = "Valentina Rojas",
                fecha = "12/10/2026",
                hora = "16:00",
                direccionOrigen = "Aeropuerto SCL, Terminal Internacional",
                direccionDestino = "Hotel Crowne Plaza, Av. Libertador B. O'Higgins 136",
                latitudOrigen = -33.3928,
                longitudOrigen = -70.7856,
                latitudDestino = -33.4417,
                longitudDestino = -70.6541,
                numeroVuelo = "AA-945",
                estado = "Pendiente"
            ),
            ViajeEntity(
                id = "VJ-002",
                nombrePasajero = "Ana Torres",
                fecha = "12/10/2026",
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
                id = "VJ-006",
                nombrePasajero = "Francisca Muñoz",
                fecha = "12/10/2026",
                hora = "10:45",
                direccionOrigen = "Hotel Sheraton, Av. Santa Maria 1742",
                direccionDestino = "Centro Empresarial, Av. Apoquindo 3000",
                latitudOrigen = -33.4285,
                longitudOrigen = -70.6157,
                latitudDestino = -33.4180,
                longitudDestino = -70.5980,
                estado = "Asignado"
            ),
            ViajeEntity(
                id = "VJ-003",
                nombrePasajero = "Roberto Fuentes",
                fecha = "12/10/2026",
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
                id = "VJ-007",
                nombrePasajero = "Patricio Soto",
                fecha = "12/10/2026",
                hora = "12:30",
                direccionOrigen = "Oficina BHP, Cerro El Plomo 6000",
                direccionDestino = "Aeropuerto SCL, Terminal Nacional",
                latitudOrigen = -33.4095,
                longitudOrigen = -70.5748,
                latitudDestino = -33.3928,
                longitudDestino = -70.7856,
                numeroVuelo = "LA-350",
                estado = "En Curso"
            ),
            ViajeEntity(
                id = "VJ-004",
                nombrePasajero = "Maria Gonzalez",
                fecha = "12/10/2026",
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
                id = "VJ-009",
                nombrePasajero = "Camila Herrera",
                fecha = "12/10/2026",
                hora = "06:30",
                direccionOrigen = "Hotel Hyatt, Av. Kennedy 4601",
                direccionDestino = "Aeropuerto SCL, Terminal Internacional",
                latitudOrigen = -33.4010,
                longitudOrigen = -70.5830,
                latitudDestino = -33.3928,
                longitudDestino = -70.7856,
                numeroVuelo = "KL-702",
                estado = "Finalizado"
            ),
            ViajeEntity(
                id = "VJ-010",
                nombrePasajero = "Andres Pereira",
                fecha = "12/10/2026",
                hora = "05:45",
                direccionOrigen = "Residencia, Vitacura 4380",
                direccionDestino = "Aeropuerto SCL, Terminal Nacional",
                latitudOrigen = -33.3985,
                longitudOrigen = -70.5915,
                latitudDestino = -33.3928,
                longitudDestino = -70.7856,
                numeroVuelo = "LA-120",
                estado = "Finalizado"
            )
        )
        viajeDao.insertarViajes(viajesFicticios)
    }
}