package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.ViajeEntity
import com.example.myapplication.ui.theme.AzulMarinoPrimary
import com.example.myapplication.ui.theme.DoradoPrimary
import com.example.myapplication.viewmodel.AdministradorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministradorScreen(
    navController: NavController,
    viewModel: AdministradorViewModel
) {
    val viajesPendientes by viewModel.viajes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Administrador", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulMarinoPrimary)
            )
        },
        containerColor = Color(0xFFF9F8F6)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Solicitudes Pendientes: ${viajesPendientes.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AzulMarinoPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (viajesPendientes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay solicitudes pendientes", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(viajesPendientes) { viaje ->
                        SolicitudAdminCard(
                            viaje = viaje,
                            onAsignarClick = { viewModel.asignarConductor(viaje) },
                            onRechazarClick = { viewModel.rechazarViaje(viaje) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitudAdminCard(
    viaje: ViajeEntity,
    onAsignarClick: () -> Unit,
    onRechazarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viaje.id,
                    fontWeight = FontWeight.Black,
                    color = AzulMarinoPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = "PENDIENTE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color(0xFFE65100),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))

            DetalleViajeRow(icono = Icons.Default.Person, texto = viaje.nombrePasajero, destacado = true)
            Spacer(modifier = Modifier.height(8.dp))
            DetalleViajeRow(icono = Icons.Default.LocationOn, texto = "Origen: ${viaje.direccionOrigen}")
            Spacer(modifier = Modifier.height(4.dp))
            DetalleViajeRow(icono = Icons.Default.ArrowForward, texto = "Destino: ${viaje.direccionDestino}")
            Spacer(modifier = Modifier.height(8.dp))
            DetalleViajeRow(icono = Icons.Default.CalendarToday, texto = "${viaje.fecha} - ${viaje.hora} hrs")

            if (!viaje.numeroVuelo.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                DetalleViajeRow(icono = Icons.Default.AirplanemodeActive, texto = "Vuelo: ${viaje.numeroVuelo}")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRechazarClick,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                ) {
                    Text("Rechazar", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onAsignarClick,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DoradoPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Asignar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetalleViajeRow(icono: ImageVector, texto: String, destacado: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = if (destacado) AzulMarinoPrimary else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (destacado) FontWeight.Bold else FontWeight.Normal,
            color = if (destacado) AzulMarinoPrimary else Color.DarkGray
        )
    }
}