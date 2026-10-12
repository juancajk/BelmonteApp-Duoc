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

/**
 * ViewModel para la pantalla del conductor.
 * Responsabilidad Constanza: Gestion de viajes con MVVM + GPS en tiempo real.
 *
 * Gestiona:
 * - Lista de viajes desde Room (offline-first)
 * - Filtros por estado (Pendiente, Asignado, En Curso, Finalizado)
 * - Transiciones de estado del viaje
 * - Ubicacion GPS del conductor via FusedLocationProviderClient
 *
 * Utiliza AndroidViewModel para acceder al contexto de la app (Room + GPS).
 */
class ConductorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViajeRepository
    private val locationHelper: LocationHelper

    // === Estados reactivos (StateFlow para Compose) ===

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

    /**
     * Carga los datos ficticios si la base esta vacia,
     * luego observa los viajes del dia.
     */
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

    /**
     * Inicia el GPS del conductor usando FusedLocationProviderClient.
     * Obtiene ubicacion inicial y luego actualizaciones en tiempo real.
     */
    private fun iniciarGps() {
        if (!locationHelper.tienePermisoUbicacion()) {
            _gpsActivo.value = false
            return
        }

        // Obtener ultima ubicacion conocida
        locationHelper.obtenerUltimaUbicacion { location ->
            _ubicacionConductor.value = location
            _gpsActivo.value = location != null
        }

        // Iniciar tracking en tiempo real
        jobGps = viewModelScope.launch {
            locationHelper.obtenerUbicacionEnTiempoReal(intervaloMs = 15_000L)
                .collect { location ->
                    _ubicacionConductor.value = location
                    _gpsActivo.value = true
                }
        }
    }

    /**
     * Reintenta activar el GPS (llamar despues de obtener permisos).
     */
    fun reintentarGps() {
        jobGps?.cancel()
        iniciarGps()
    }

    /**
     * Calcula la distancia entre el conductor y el destino de un viaje.
     * @return Distancia en km, o null si no hay ubicacion GPS.
     */
    fun distanciaAlDestino(viaje: ViajeEntity): Double? {
        val ubicacion = _ubicacionConductor.value ?: return null
        if (viaje.latitudDestino == 0.0 && viaje.longitudDestino == 0.0) return null
        return locationHelper.calcularDistanciaKm(
            ubicacion.latitude, ubicacion.longitude,
            viaje.latitudDestino, viaje.longitudDestino
        )
    }

    /**
     * Observa los viajes segun el filtro actual.
     * Cancela el job anterior para evitar multiples colectores.
     */
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

    /**
     * Cambia el filtro de estados y recarga los viajes.
     */
    fun cambiarFiltro(nuevoFiltro: String) {
        _filtroActual.value = nuevoFiltro
        observarViajes()
    }

    /**
     * Actualiza el estado de un viaje siguiendo el flujo secuencial:
     * Pendiente -> Asignado -> En Curso -> Finalizado
     */
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

    /**
     * Permite retroceder el estado de un viaje (por error del conductor).
     */
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
