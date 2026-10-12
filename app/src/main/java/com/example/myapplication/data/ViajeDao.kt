package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones CRUD sobre los viajes.
 * Responsabilidad Constanza: Persistencia local con Room/SQLite.
 *
 * Utiliza Flow de Kotlin para reactividad en tiempo real con Compose,
 * permitiendo que la UI se actualice automaticamente cuando cambian
 * los datos en la base de datos local.
 */
@Dao
interface ViajeDao {

    /** Obtener todos los viajes ordenados por fecha y hora */
    @Query("SELECT * FROM viajes ORDER BY fecha DESC, hora DESC")
    fun obtenerTodosLosViajes(): Flow<List<ViajeEntity>>

    /** Obtener viajes filtrados por estado */
    @Query("SELECT * FROM viajes WHERE estado = :estado ORDER BY fecha DESC, hora DESC")
    fun obtenerViajesPorEstado(estado: String): Flow<List<ViajeEntity>>

    /** Obtener viajes del dia actual */
    @Query("SELECT * FROM viajes WHERE fecha = :fechaHoy ORDER BY hora ASC")
    fun obtenerViajesDelDia(fechaHoy: String): Flow<List<ViajeEntity>>

    /** Obtener un viaje por su ID */
    @Query("SELECT * FROM viajes WHERE id = :viajeId")
    suspend fun obtenerViajePorId(viajeId: String): ViajeEntity?

    /** Insertar un viaje (reemplaza si ya existe por PK) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarViaje(viaje: ViajeEntity)

    /** Insertar multiples viajes de una vez */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarViajes(viajes: List<ViajeEntity>)

    /** Actualizar un viaje existente */
    @Update
    suspend fun actualizarViaje(viaje: ViajeEntity)

    /** Actualizar solo el estado de un viaje */
    @Query("UPDATE viajes SET estado = :nuevoEstado WHERE id = :viajeId")
    suspend fun actualizarEstado(viajeId: String, nuevoEstado: String)

    /** Eliminar todos los viajes (para sincronizacion completa) */
    @Query("DELETE FROM viajes")
    suspend fun eliminarTodos()

    /** Contar viajes por estado (para resumen) */
    @Query("SELECT COUNT(*) FROM viajes WHERE estado = :estado")
    suspend fun contarPorEstado(estado: String): Int

    /** Contar total de viajes (para verificar si la BD esta vacia) */
    @Query("SELECT COUNT(*) FROM viajes")
    suspend fun contarTotal(): Int
}
