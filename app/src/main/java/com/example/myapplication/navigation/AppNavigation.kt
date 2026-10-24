package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.AdministradorScreen
import com.example.myapplication.view.EscanerQRScreen
import com.example.myapplication.view.HomeScreen
import com.example.myapplication.view.LoginAdministradorScreen
import com.example.myapplication.view.LoginConductorScreen
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.OnboardingScreen
import com.example.myapplication.view.RutasConductorScreen
import com.example.myapplication.view.SeleccionRolScreen
import com.example.myapplication.view.SolicitudScreen
import com.example.myapplication.view.SplashScreen
import com.example.myapplication.viewmodel.ConductorViewModel
import com.example.myapplication.viewmodel.SolicitudViewModel
import com.example.myapplication.viewmodel.AdministradorViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val solicitudViewModel: SolicitudViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToForm = {
                    navController.navigate(Routes.SELECCION_ROL) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SELECCION_ROL) {
            SeleccionRolScreen(
                onSeleccionarPasajero = {
                    navController.navigate(Routes.SOLICITUD)
                },
                onSeleccionarConductor = {
                    navController.navigate(Routes.LOGIN_CONDUCTOR)
                },
                onSeleccionarAdministrador = {
                    navController.navigate("login_admin")
                }
            )
        }

        composable("login_admin") {
            LoginAdministradorScreen(
                onLoginSuccess = {
                    navController.navigate("administrador") {
                        popUpTo("login_admin") { inclusive = true }
                    }
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SOLICITUD) {
            SolicitudScreen(viewModel = solicitudViewModel, navController = navController)
        }

        composable(Routes.LOGIN_CONDUCTOR) {
            LoginConductorScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.RUTAS_CONDUCTOR) {
                        popUpTo(Routes.LOGIN_CONDUCTOR) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.RUTAS_CONDUCTOR) {
            val conductorViewModel: ConductorViewModel = viewModel()
            RutasConductorScreen(viewModel = conductorViewModel, navController = navController)
        }

        composable(Routes.EDITAR_VIAJE) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val conductorViewModel: ConductorViewModel = viewModel()
            com.example.myapplication.view.EditarViajeScreen(
                viajeId = id,
                viewModel = conductorViewModel,
                navController = navController
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME)
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen()
        }

        composable(Routes.ESCANER_QR) {
            EscanerQRScreen(navController = navController)
        }

        composable("administrador") {
            val adminViewModel: AdministradorViewModel = viewModel()
            AdministradorScreen(navController = navController, viewModel = adminViewModel)
        }
    }
}