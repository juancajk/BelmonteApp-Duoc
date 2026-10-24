package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.ViajeEntity
import com.example.myapplication.data.ViajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdministradorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViajeRepository


    private val _viajes = MutableStateFlow<List<ViajeEntity>>(emptyList())
    val viajes: StateFlow<List<ViajeEntity>> = _viajes.asStateFlow()

    init {
        val dao = AppDatabase.getInstance(application).viajeDao()
        repository = ViajeRepository(dao)
        cargarSolicitudesPendientes()
    }

    private fun cargarSolicitudesPendientes() {
        viewModelScope.launch {

            repository.obtenerViajesPendientes().collect { lista ->
                _viajes.value = lista
            }
        }
    }

    fun rechazarViaje(viaje: ViajeEntity) {
        viewModelScope.launch {
            repository.eliminarViaje(viaje)

        }
    }

    fun asignarConductor(viaje: ViajeEntity) {
        viewModelScope.launch {

            val viajeActualizado = viaje.copy(estado = "Asignado")
            repository.actualizarViaje(viajeActualizado)
        }
    }
}