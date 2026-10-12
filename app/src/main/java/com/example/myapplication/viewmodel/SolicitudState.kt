package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ViajeDao
import com.example.myapplication.data.ViajeEntity
import kotlinx.coroutines.launch
import java.util.UUID

sealed class SolicitudState {
    object Idle : SolicitudState()
    object Loading : SolicitudState()
    object Success : SolicitudState()
    data class Error(val message: String) : SolicitudState()
}

class SolicitudViewModel : ViewModel() {
    var nombre by mutableStateOf("")
    var origen by mutableStateOf("")
    var destino by mutableStateOf("")
    var vuelo by mutableStateOf("")
    var fecha by mutableStateOf("")
    var hora by mutableStateOf("")
    var fotoUri by mutableStateOf<String?>(null)

    var intentoEnviar by mutableStateOf(false)
        private set

    var state by mutableStateOf<SolicitudState>(SolicitudState.Idle)
        private set

    fun isValid(): Boolean {
        return nombre.isNotBlank() && origen.isNotBlank() &&
                destino.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank()
    }


    fun enviarSolicitud(viajeDao: ViajeDao) {
        intentoEnviar = true

        if (!isValid()) {
            state = SolicitudState.Error("Por favor, completa los campos en rojo.")
            return
        }

        viewModelScope.launch {
            state = SolicitudState.Loading
            try {

                val nuevoViajeEntity = ViajeEntity(
                    id = UUID.randomUUID().toString(),
                    nombrePasajero = nombre,
                    fecha = fecha,
                    hora = hora,
                    direccionOrigen = origen,
                    direccionDestino = destino,
                    numeroVuelo = vuelo.ifBlank { null },
                    estado = "Pendiente",
                    fotoRefUri = fotoUri
                )


                viajeDao.insertarViaje(nuevoViajeEntity)


                state = SolicitudState.Success

            } catch (e: Exception) {
                state = SolicitudState.Error("Error al guardar en BD: ${e.localizedMessage}")
            }
        }
    }
}
