package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.Viaje

/**
 * Entidad Room que representa un viaje/traslado en la base de datos local SQLite.
 * Responsabilidad Constanza: Persistencia offline-first con Room.
 *
 * Permite que el conductor acceda a sus viajes asignados aun sin conexion
 * a internet, cumpliendo con el requisito de disponibilidad offline.
 *
 * Modelo Unificado (Responsabilidad Transversal):
 * Incluye funciones de conversion entre ViajeEntity (Room/local) y
 * Viaje (API/Retrofit) para mantener coherencia entre ambos modulos.
 */
@Entity(tableName = "viajes")
data class ViajeEntity(
    @PrimaryKey
    val id: String,
    val nombrePasajero: String,
    val fecha: String,
    val hora: String,
    val direccionOrigen: String,
    val direccionDestino: String,
    val latitudOrigen: Double = 0.0,
    val longitudOrigen: Double = 0.0,
    val latitudDestino: Double = 0.0,
    val longitudDestino: Double = 0.0,
    val numeroVuelo: String? = null,
    val estado: String = "Pendiente", // Pendiente, Asignado, En Curso, Finalizado
    val fotoRefUri: String? = null     // URI de foto de referencia (compartido con Parte 1)
) {
    /**
     * Convierte la entidad Room a modelo de dominio compartido.
     * Modelo Unificado - Transversal entre Parte 1 y Parte 2.
     */
    fun toViaje(): Viaje = Viaje(
        id = id,
        nombrePasajero = nombrePasajero,
        fecha = fecha,
        hora = hora,
        direccionOrigen = direccionOrigen,
        direccionDestino = direccionDestino,
        numeroVuelo = numeroVuelo,
        estado = estado,
        fotoRefUri = fotoRefUri
    )

    companion object {
        /**
         * Crea una entidad Room desde el modelo de dominio compartido.
         */
        fun fromViaje(viaje: Viaje): ViajeEntity = ViajeEntity(
            id = viaje.id,
            nombrePasajero = viaje.nombrePasajero,
            fecha = viaje.fecha,
            hora = viaje.hora,
            direccionOrigen = viaje.direccionOrigen,
            direccionDestino = viaje.direccionDestino,
            numeroVuelo = viaje.numeroVuelo,
            estado = viaje.estado,
            fotoRefUri = viaje.fotoRefUri
        )
    }
}
