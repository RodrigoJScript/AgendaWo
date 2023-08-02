package com.rodrigojscript.agendawow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodrigojscript.agendawow.ui.screens.GuardarDatos
import com.rodrigojscript.agendawow.ui.screens.MostrarDatos
import com.rodrigojscript.agendawow.ui.screens.SplashScreen
import com.rodrigojscript.agendawow.ui.theme.AgendaWowTheme
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val agendaViewModel: AgendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
        setContent {
            AgendaWowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("inicio") { Inicio(navController) }
                        composable("mostrarDatos") { MostrarDatos(agendaViewModel) }
                        composable("guardarDatos") {
                            GuardarDatos(
                                agendaViewModel = agendaViewModel
                            )
                        }
                        composable("splash") { SplashScreen(navController) }
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Inicio(navController: NavHostController) {
        var auth by rememberSaveable { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "AgendaWoW")
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.dos),
                    contentDescription = "Logo"
                )
                Button(
                    onClick = { navController.navigate("guardarDatos") },
                    enabled = auth,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Guardar datos")
                }
                Button(
                    onClick = { navController.navigate("mostrarDatos") },
                    enabled = auth,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Mostrar datos")
                }
                Button(
                    onClick = {
                        authenticate {
                            auth = !auth
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (auth) "Cerrar sesión" else "Iniciar sesión")
                }
            }
        }
    }


}

