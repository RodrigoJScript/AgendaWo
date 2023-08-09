package com.rodrigojscript.agendawow.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rodrigojscript.agendawow.model.database.AgendaEntity
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel

@Composable
fun MostrarDatos(agendaViewModel: AgendaViewModel) {
    val list: List<AgendaEntity.Contacto> =
        agendaViewModel.getAllData().observeAsState(listOf()).value
    val showDialog = remember { mutableStateOf(false) }
    val showDeleteAllDialog = remember { mutableStateOf(false) }
    var selectedContact: AgendaEntity.Contacto? by remember { mutableStateOf(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        itemsIndexed(list) { index, item ->
            CustomCardData(item, agendaViewModel, onEditClicked = { contact ->
                selectedContact = contact
                showDialog.value = true
            })
        }

        item {
            Button(
                onClick = { showDeleteAllDialog.value = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete All Contacts")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Delete All Contacts", color = Color.White)
            }
        }
    }
    if (showDeleteAllDialog.value) {
        AlertDialog(
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete all contacts?") },
            onDismissRequest = { showDeleteAllDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        agendaViewModel.deleteAllData()
                        showDeleteAllDialog.value = false
                        // Agrega aquí el código para mostrar un Toast o una confirmación de eliminación exitosa
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = "Delete All")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteAllDialog.value = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    if (showDialog.value && selectedContact != null) {
        EditContactDialogCustom(
            contact = selectedContact!!,
            onEdit = { editedContact ->
                agendaViewModel.updateData(editedContact)
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false }
        )
    }
}

@Composable
fun CustomCardData(
    item: AgendaEntity.Contacto,
    agendaViewModel: AgendaViewModel,
    onEditClicked: (AgendaEntity.Contacto) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            title = { Text(text = "Estás a punto de eliminar un registro") },
            text = {
                Text(text = "¿Eliminar?")
            },
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        agendaViewModel.deleteData(item)
                        openDialog.value = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = "Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog.value = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        EditContactDialogCustom(contact = item, onEdit = { editedContact ->
            agendaViewModel.updateData(editedContact)
            showDialog.value = false
        }, onDismiss = {
            showDialog.value = false
        }
        )
    }
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item.photoUri?.let { uriString ->
                val context = LocalContext.current
                val uri = Uri.parse(uriString)
                val bitmap = loadBitmap(context, uri)
                bitmap?.let { image ->
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "Contact Photo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nombre: ${item.name}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Apellido: ${item.lastName}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Email: ${item.email}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Telefono: ${item.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )
            }
            Row {
                IconButton(onClick = { openDialog.value = true }) {
                    Icon(Icons.Filled.Delete, "")
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = { onEditClicked(item) }) {
                    Icon(Icons.Filled.Edit, "")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactDialogCustom(
    contact: AgendaEntity.Contacto, onEdit: (AgendaEntity.Contacto) -> Unit, onDismiss: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf(contact.name) }
    var lastName by rememberSaveable { mutableStateOf(contact.lastName) }
    var email by rememberSaveable { mutableStateOf(contact.email) }
    var phone by rememberSaveable { mutableStateOf(contact.phone.toString()) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Editar contacto", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                TextField(value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") })
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            val editedContact = contact.copy(
                                name = name, lastName = lastName, email = email, phone = try {
                                    phone.toInt()
                                } catch (e: NumberFormatException) {
                                    contact.phone
                                }
                            )
                            onEdit(editedContact)
                            onDismiss()
                        }, modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Guardar cambios", color = Color.Black)
                    }
                    Button(
                        onClick = onDismiss, modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Cancelar", color = Color.Black)
                    }
                }
            }
        }
    }
}
