package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TagsScreen() {
    var tags by remember { mutableStateOf(listOf("Bonpreu-Esclat", "Mercadona")) }
    var showDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Etiquetas", style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = { showDialog = true }) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
        LazyColumn {
            items(tags) { tag ->
                Text(tag, Modifier.fillMaxWidth().padding(vertical = 12.dp))
                HorizontalDivider()
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nueva etiqueta") },
            text = { OutlinedTextField(value = newTag, onValueChange = { newTag = it }) },
            confirmButton = {
                TextButton(onClick = {
                    if (newTag.isNotBlank()) tags = tags + newTag
                    newTag = ""
                    showDialog = false
                }) { Text("Añadir") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        )
    }
}