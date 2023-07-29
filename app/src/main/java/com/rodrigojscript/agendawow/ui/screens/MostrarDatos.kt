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
import androidx.navigation.NavController
import com.rodrigojscript.agendawow.model.database.AgendaEntity
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel

@Composable
fun MostrarDatos(navController: NavController, agendaViewModel: AgendaViewModel) {
    val list: List<AgendaEntity.Contacto> =
        agendaViewModel.getAllData().observeAsState(listOf()).value
    val showDialog = remember { mutableStateOf(false) }
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
                onClick = { agendaViewModel.deleteAllData() }, modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete All Contacts")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Delete All Contacts")
            }
        }
    }

    if (showDialog.value && selectedContact != null) {
        EditContactDialogCustom(contact = selectedContact!!, onEdit = { editedContact ->
            agendaViewModel.updateData(editedContact)
            showDialog.value = false
        }, onDismiss = { showDialog.value = false })
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
        AlertDialog(title = { Text(text = "Estas a punto de eliminar un registro") }, text = {
            Text(
                text = "¿Eliminar?"
            )
        }, onDismissRequest = { openDialog.value = false }, confirmButton = {
            TextButton(onClick = { // (4)
                agendaViewModel.deleteData(item)
                openDialog.value = false
            }) {
                Text(text = "Eliminar", color = Color.Black)
            }
        }, dismissButton = {
            TextButton(onClick = { // (5)
                openDialog.value = false
            }) {
                Text(text = "Cancelar", color = Color.Black)
            }
        })
    }

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        EditContactDialogCustom(contact = item, onEdit = { editedContact ->
            agendaViewModel.updateData(editedContact)
            showDialog.value = false // Cerrar el diálogo después de guardar los cambios
        }, onDismiss = {
            showDialog.value = false
        } // Cerrar el diálogo al hacer clic en "Cancelar"
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
                    text = "Ticket: $${item.name}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nota 1: $${item.lastName}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nota 2: $${item.email}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nota 3: $${item.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { openDialog.value = true }) {
                Icon(Icons.Filled.Delete, "")
            }
            IconButton(onClick = { onEditClicked(item) }) {
                Icon(Icons.Filled.Edit, "")
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
