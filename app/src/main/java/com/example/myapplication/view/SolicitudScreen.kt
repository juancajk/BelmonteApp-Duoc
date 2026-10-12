package com.example.myapplication.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.AzulMarinoPrimary
import com.example.myapplication.viewmodel.SolicitudState
import com.example.myapplication.viewmodel.SolicitudViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudScreen(viewModel: SolicitudViewModel, navController: NavController) {
    val context = LocalContext.current

    val database = com.example.myapplication.data.AppDatabase.getInstance(context)
    val viajeDao = database.viajeDao()

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            viewModel.fecha = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val minStr = if (minute < 10) "0$minute" else "$minute"
            viewModel.hora = "$hourOfDay:$minStr"
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) viewModel.fotoUri = "uri_temporal_foto"
    }


    val colorFondo = Color(0xFFF2F5F8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Nuevo Traslado",
                    style = MaterialTheme.typography.headlineMedium,
                    color = AzulMarinoPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 32.dp, top = 16.dp)
                )

                OutlinedTextField(
                    value = viewModel.nombre,
                    onValueChange = { viewModel.nombre = it },
                    label = { Text("Nombre del Pasajero *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                    isError = viewModel.intentoEnviar && viewModel.nombre.isBlank(),
                    supportingText = { if (viewModel.intentoEnviar && viewModel.nombre.isBlank()) Text("Obligatorio") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.origen,
                    onValueChange = { viewModel.origen = it },
                    label = { Text("Punto de Origen *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
                    isError = viewModel.intentoEnviar && viewModel.origen.isBlank(),
                    supportingText = { if (viewModel.intentoEnviar && viewModel.origen.isBlank()) Text("Obligatorio") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.destino,
                    onValueChange = { viewModel.destino = it },
                    label = { Text("Destino *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
                    isError = viewModel.intentoEnviar && viewModel.destino.isBlank(),
                    supportingText = { if (viewModel.intentoEnviar && viewModel.destino.isBlank()) Text("Obligatorio") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.vuelo,
                    onValueChange = { viewModel.vuelo = it },
                    label = { Text("Número de vuelo (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.weight(1f).padding(end = 6.dp).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = if (viewModel.intentoEnviar && viewModel.fecha.isBlank()) MaterialTheme.colorScheme.error else AzulMarinoPrimary
                        )
                    ) {
                        Text(if (viewModel.fecha.isEmpty()) "Fecha *" else viewModel.fecha)
                    }
                    OutlinedButton(
                        onClick = { timePickerDialog.show() },
                        modifier = Modifier.weight(1f).padding(start = 6.dp).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = if (viewModel.intentoEnviar && viewModel.hora.isBlank()) MaterialTheme.colorScheme.error else AzulMarinoPrimary
                        )
                    ) {
                        Text(if (viewModel.hora.isEmpty()) "Hora *" else viewModel.hora)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                        cameraLauncher.launch(null)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B))
                ) {
                    Text(if (viewModel.fotoUri == null) "📸  Tomar Foto Referencia" else "✓  Foto Capturada")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.enviarSolicitud(viajeDao) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = viewModel.state !is SolicitudState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarinoPrimary)
                ) {
                    Text("Confirmar Solicitud", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                when (val state = viewModel.state) {
                    is SolicitudState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    is SolicitudState.Success -> Text("¡Solicitud enviada con éxito!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    is SolicitudState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
                    else -> {}
                }
            }
        }
    }
}