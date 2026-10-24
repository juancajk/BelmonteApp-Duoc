package com.example.myapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AzulMarinoPrimary
import com.example.myapplication.ui.theme.DoradoDark
import com.example.myapplication.ui.theme.DoradoPrimary

@Composable
fun SeleccionRolScreen(
    onSeleccionarPasajero: () -> Unit,
    onSeleccionarConductor: () -> Unit,
    onSeleccionarAdministrador: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF9F8F6), Color(0xFFE8E0D5))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .background(Color.Black, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Belmonte SpA",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DoradoPrimary
                    )
                    Text(
                        text = "Transporte Corporativo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Seleccione su perfil",
                style = MaterialTheme.typography.bodyLarge,
                color = AzulMarinoPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(48.dp))

            RolCard(
                titulo = "Soy Pasajero",
                descripcion = "Solicitar un traslado corporativo",
                icono = Icons.Default.Person,
                colorIcono = DoradoPrimary,
                onClick = onSeleccionarPasajero
            )

            Spacer(modifier = Modifier.height(20.dp))

            RolCard(
                titulo = "Soy Conductor",
                descripcion = "Ver mis viajes asignados",
                icono = Icons.Default.DirectionsCar,
                colorIcono = DoradoPrimary,
                onClick = onSeleccionarConductor
            )

            Spacer(modifier = Modifier.height(20.dp))

            RolCard(
                titulo = "Soy Administrador",
                descripcion = "Gestión de solicitudes",
                icono = Icons.Default.Settings,
                colorIcono = DoradoPrimary,
                onClick = onSeleccionarAdministrador
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "MVP Educativo - Datos Ficticios",
                style = MaterialTheme.typography.labelSmall,
                color = AzulMarinoPrimary.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RolCard(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    colorIcono: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                modifier = Modifier.size(48.dp),
                tint = colorIcono
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarinoPrimary
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DoradoDark
                )
            }
        }
    }
}