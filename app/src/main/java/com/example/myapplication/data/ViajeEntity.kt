package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.Viaje


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
    val estado: String = "Pendiente",
    val fotoRefUri: String? = null
) {

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
