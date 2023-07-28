package com.rodrigojscript.agendawow.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }
    Column {
        TextField(value = name, onValueChange = { name = it })
        TextField(value = lastName, onValueChange = { lastName = it })
        TextField(value = email, onValueChange = { email = it })
        TextField(value = phone, onValueChange = { phone = it })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { launcher.launch("image/*") }) {
            Icon(Icons.Default.Add, contentDescription = "Select Photo")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add Photo")
        }

        selectedImageUri?.let { uri ->
            val bitmap = loadBitmap(context, uri)
            bitmap?.let { image ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = "Selected Photo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }
        }
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
private fun loadBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let { stream ->
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            inputStream.close()
            bitmap
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
