package com.example.myapplication

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.viewmodel.SolicitudViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SolicitudViewModelTest {

    private lateinit var viewModel: SolicitudViewModel

    @Before
    fun setup() {

        val application = ApplicationProvider.getApplicationContext<Application>()
        viewModel = SolicitudViewModel(application)
    }

    @Test
    fun prueba1_al_iniciar_el_viewmodel_los_campos_deben_estar_vacios() {
        assertEquals("", viewModel.nombre)
        assertEquals("", viewModel.origen)
        assertEquals("", viewModel.destino)
        assertEquals("", viewModel.fecha)
        assertEquals("", viewModel.hora)
    }

    @Test
    fun prueba2_al_capturar_datos_las_variables_se_actualizan_correctamente() {
        viewModel.nombre = "Juan Carlos"
        viewModel.origen = "Duoc UC San Joaquín"
        viewModel.destino = "Aeropuerto AMB"

        assertEquals("Juan Carlos", viewModel.nombre)
        assertEquals("Duoc UC San Joaquín", viewModel.origen)
        assertEquals("Aeropuerto AMB", viewModel.destino)
    }

    @Test
    fun prueba3_la_uri_de_la_foto_debe_ser_nula_antes_de_abrir_camara() {
        assertNull("La foto debería ser null al inicio", viewModel.fotoUri)
    }
}