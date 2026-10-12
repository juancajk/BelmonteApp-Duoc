package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ViajeDao {


    @Query("SELECT * FROM viajes ORDER BY fecha DESC, hora DESC")
    fun obtenerTodosLosViajes(): Flow<List<ViajeEntity>>


    @Query("SELECT * FROM viajes WHERE estado = :estado ORDER BY fecha DESC, hora DESC")
    fun obtenerViajesPorEstado(estado: String): Flow<List<ViajeEntity>>


    @Query("SELECT * FROM viajes WHERE fecha = :fechaHoy ORDER BY hora ASC")
    fun obtenerViajesDelDia(fechaHoy: String): Flow<List<ViajeEntity>>


    @Query("SELECT * FROM viajes WHERE id = :viajeId")
    suspend fun obtenerViajePorId(viajeId: String): ViajeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarViaje(viaje: ViajeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarViajes(viajes: List<ViajeEntity>)

    @Update
    suspend fun actualizarViaje(viaje: ViajeEntity)

    @Query("UPDATE viajes SET estado = :nuevoEstado WHERE id = :viajeId")
    suspend fun actualizarEstado(viajeId: String, nuevoEstado: String)

    @Query("DELETE FROM viajes")
    suspend fun eliminarTodos()

    @Query("SELECT COUNT(*) FROM viajes WHERE estado = :estado")
    suspend fun contarPorEstado(estado: String): Int

    @Query("SELECT COUNT(*) FROM viajes")
    suspend fun contarTotal(): Int
}
