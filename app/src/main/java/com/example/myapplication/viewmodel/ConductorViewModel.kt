package com.example.myapplication.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.ViajeEntity
import com.example.myapplication.data.ViajeRepository
import com.example.myapplication.util.LocationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ConductorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViajeRepository
    private val locationHelper: LocationHelper


    private val _viajes = MutableStateFlow<List<ViajeEntity>>(emptyList())
    val viajes: StateFlow<List<ViajeEntity>> = _viajes.asStateFlow()

    private val _filtroActual = MutableStateFlow("Todos")
    val filtroActual: StateFlow<String> = _filtroActual.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _ubicacionConductor = MutableStateFlow<Location?>(null)
    val ubicacionConductor: StateFlow<Location?> = _ubicacionConductor.asStateFlow()

    private val _gpsActivo = MutableStateFlow(false)
    val gpsActivo: StateFlow<Boolean> = _gpsActivo.asStateFlow()

    private var jobObservarViajes: Job? = null
    private var jobGps: Job? = null

    init {
        val dao = AppDatabase.getInstance(application).viajeDao()
        repository = ViajeRepository(dao)
        locationHelper = LocationHelper(application)
        cargarDatosIniciales()
        iniciarGps()
    }


    private fun cargarDatosIniciales() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.cargarDatosFicticios()
                observarViajes()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar datos: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun iniciarGps() {
        if (!locationHelper.tienePermisoUbicacion()) {
            _gpsActivo.value = false
            return
        }

        locationHelper.obtenerUltimaUbicacion { location ->
            _ubicacionConductor.value = location
            _gpsActivo.value = location != null
        }

        jobGps = viewModelScope.launch {
            locationHelper.obtenerUbicacionEnTiempoReal(intervaloMs = 15_000L)
                .collect { location ->
                    _ubicacionConductor.value = location
                    _gpsActivo.value = true
                }
        }
    }


    fun reintentarGps() {
        jobGps?.cancel()
        iniciarGps()
    }


    fun distanciaAlDestino(viaje: ViajeEntity): Double? {
        val ubicacion = _ubicacionConductor.value ?: return null
        if (viaje.latitudDestino == 0.0 && viaje.longitudDestino == 0.0) return null
        return locationHelper.calcularDistanciaKm(
            ubicacion.latitude, ubicacion.longitude,
            viaje.latitudDestino, viaje.longitudDestino
        )
    }


    private fun observarViajes() {
        jobObservarViajes?.cancel()
        jobObservarViajes = viewModelScope.launch {
            val fechaHoy = SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(Date())
            val flow = when (_filtroActual.value) {
                "Todos" -> repository.obtenerViajesDelDia(fechaHoy)
                else -> repository.obtenerViajesPorEstado(_filtroActual.value)
            }
            flow.collect { lista ->
                _viajes.value = lista
            }
        }
    }


    fun cambiarFiltro(nuevoFiltro: String) {
        _filtroActual.value = nuevoFiltro
        observarViajes()
    }


    fun avanzarEstado(viaje: ViajeEntity) {
        val nuevoEstado = when (viaje.estado) {
            "Pendiente" -> "Asignado"
            "Asignado" -> "En Curso"
            "En Curso" -> "Finalizado"
            else -> return // Finalizado no avanza
        }

        viewModelScope.launch {
            try {
                repository.actualizarEstado(viaje.id, nuevoEstado)
                _mensaje.value = "Viaje ${viaje.id}: $nuevoEstado"
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar: ${e.localizedMessage}"
            }
        }
    }


    fun retrocederEstado(viaje: ViajeEntity) {
        val nuevoEstado = when (viaje.estado) {
            "Asignado" -> "Pendiente"
            "En Curso" -> "Asignado"
            "Finalizado" -> "En Curso"
            else -> return
        }

        viewModelScope.launch {
            try {
                repository.actualizarEstado(viaje.id, nuevoEstado)
                _mensaje.value = "Viaje ${viaje.id} revertido a: $nuevoEstado"
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    override fun onCleared() {
        super.onCleared()
        jobGps?.cancel()
        jobObservarViajes?.cancel()
    }
}
