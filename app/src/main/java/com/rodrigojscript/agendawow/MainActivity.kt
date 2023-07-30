package com.rodrigojscript.agendawow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodrigojscript.agendawow.ui.screens.GuardarDatos
import com.rodrigojscript.agendawow.ui.screens.MostrarDatos
import com.rodrigojscript.agendawow.ui.theme.AgendaWowTheme
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val agendaViewModel: AgendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
        setContent {
            AgendaWowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "inicio") {
                        composable("inicio") { Inicio(navController) }
                        composable("mostrarDatos") { MostrarDatos(navController, agendaViewModel) }
                        composable("guardarDatos") {
                            GuardarDatos(
                                agendaViewModel = agendaViewModel, navController
                            )
                        }
                    }
                }
            }
        }
        setupAuth()
    }

    private var canAuth = false
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private fun setupAuth() {
        if (BiometricManager.from(this)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            canAuth = true
            promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Autenticacion biometrica")
                .setSubtitle("Autenticate utilizando el sensor biometrico")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()
        }
    }

    private fun authenticate(auth: (auth: Boolean) -> Unit) {
        if (canAuth) {
            BiometricPrompt(
                this,
                ContextCompat.getMainExecutor(this),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)

                        auth(true)
                    }
                }).authenticate(promptInfo)
        } else {
            auth(true)
        }
    }

    @Composable
    fun Inicio(navController: NavHostController) {
        var auth by rememberSaveable { mutableStateOf(false) }
        Column {
            Button(onClick = { navController.navigate("guardarDatos") }, enabled = auth) {
                Text(text = "Guardar datos")
            }
            Button(onClick = { navController.navigate("mostrarDatos") }, enabled = auth) {
                Text(text = "Mostrar datos")
            }
            Button(onClick = {
                authenticate {
                    if (auth) {
                        auth = false
                    } else {
                        auth = it
                    }
                }
            }) {
                Text(text = "Autenticar")
            }
        }
    }

}

