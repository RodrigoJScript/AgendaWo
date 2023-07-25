package com.rodrigojscript.agendawow

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodrigojscript.agendawow.ui.screens.GuardarDatos
import com.rodrigojscript.agendawow.ui.screens.Inicio
import com.rodrigojscript.agendawow.ui.screens.MostrarDatos
import com.rodrigojscript.agendawow.ui.theme.AgendaWowTheme
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val agendaViewModel: AgendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
        setContent {
            AgendaWowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "inicio") {
                        composable("inicio") { Inicio(navController) }
                        composable("mostrarDatos") { MostrarDatos(navController, agendaViewModel) }
                        composable("guardarDatos") {
                            GuardarDatos(
                                agendaViewModel = agendaViewModel,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}