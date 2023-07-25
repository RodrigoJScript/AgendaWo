package com.rodrigojscript.agendawow.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Inicio(navController: NavHostController) {
    Column {
        Button(onClick = { navController.navigate("guardarDatos") }) {
            Text(text = "Guardar datos")
        }
        Button(onClick = { navController.navigate("mostrarDatos") }) {
            Text(text = "Mostrar datos")
        }
    }
}
