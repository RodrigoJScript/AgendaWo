package com.rodrigojscript.agendawow.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rodrigojscript.agendawow.model.database.AgendaEntity
import com.rodrigojscript.agendawow.viewModel.AgendaViewModel

@Composable
fun MostrarDatos(navController: NavController, agendaViewModel: AgendaViewModel) {
    val list: List<AgendaEntity.Contacto> =
        agendaViewModel.getAllData().observeAsState(listOf()).value

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        itemsIndexed(list) { _, item ->
            CustomCardData(item, agendaViewModel)
        }

        item {
            Button(
                onClick = { agendaViewModel.deleteAllData() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete All Contacts")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Delete All Contacts")
            }
        }
    }
}

@Composable
fun CustomCardData(item: AgendaEntity.Contacto, agendaViewModel: AgendaViewModel) {
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            title = { Text(text = "Estas a punto de eliminar un registro") },
            text = {
                Text(
                    text = "¿Eliminar?"
                )
            },
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = { // (4)
                    agendaViewModel.deleteData(item)
                    openDialog.value = false
                }) {
                    Text(text = "Eliminar", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { // (5)
                    openDialog.value = false
                }) {
                    Text(text = "Cancelar", color = Color.Black)
                }
            }
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
        }
    }
}