package com.rodrigojscript.agendawow.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rodrigojscript.agendawow.model.database.AgendaEntity
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardarDatos(agendaViewModel: AgendaViewModel, navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    Column {
        TextField(value = name, onValueChange = { name = it })
        TextField(value = lastName, onValueChange = { lastName = it })
        TextField(value = email, onValueChange = { email = it })
        TextField(value = phone, onValueChange = { phone = it })
        Button(onClick = {
            // Convertir el campo 'phone' de String a Int
            val phoneInt: Int = try {
                phone.toInt()
            } catch (e: NumberFormatException) {
                // Manejar el error si el usuario ingresa un valor no numérico
                // Aquí puedes mostrar un mensaje al usuario o realizar alguna otra acción apropiada
                return@Button
            }
            agendaViewModel.insertData(
                datos = AgendaEntity.Contacto(
                    id = null,
                    name = name,
                    lastName = lastName,
                    email = email,
                    phone = phoneInt
                )
            )
        }) {
            Text(text = "Guardar")
        }
    }
}