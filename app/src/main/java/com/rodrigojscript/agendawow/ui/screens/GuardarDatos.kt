package com.rodrigojscript.agendawow.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
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
import androidx.navigation.NavController
import com.rodrigojscript.agendawow.model.database.AgendaEntity
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardarDatos(agendaViewModel: AgendaViewModel, navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") })
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { launcher.launch("image/*") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Select Photo")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add Photo")
            }
            selectedImageUri?.let { uri ->
                val newImageUri: Uri? = saveImageToExternalStorage(context, uri)
                selectedImageUri = newImageUri
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
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Convertir el campo 'phone' de String a Int
                    val phoneInt: Int = try {
                        phone.toInt()
                    } catch (e: NumberFormatException) {
                        // Manejar el error si el usuario ingresa un valor no numérico
                        // Aquí puedes mostrar un mensaje al usuario o realizar alguna otra acción apropiada
                        return@Button
                    }
                    val contact = AgendaEntity.Contacto(
                        id = null,
                        name = name,
                        lastName = lastName,
                        email = email,
                        phone = phoneInt,
                        photoUri = selectedImageUri?.toString() // Guardamos la ruta de la foto como String
                    )
                    agendaViewModel.insertData(contact)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Save Contact")
            }
        }
    }
}

fun loadBitmap(context: Context, uri: Uri): Bitmap? {
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

fun saveImageToExternalStorage(context: Context, uri: Uri): Uri? {
    val outputStream: OutputStream?
    try {
        val extension = context.contentResolver.getType(uri)?.substringAfter('/')
        val fileName = "contact_image_${System.currentTimeMillis()}.${extension}"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        outputStream = FileOutputStream(file)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.copyTo(outputStream)
        }
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
