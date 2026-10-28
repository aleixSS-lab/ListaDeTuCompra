
package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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

import android.widget.Toast
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import org.insbaixcamp.listadetucompra.ui.data.TagRepository

@Composable
fun TagsScreen() {
    val tagRepository = remember { TagRepository() }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    val tags = remember {
        mutableStateListOf<String>()
    }

    DisposableEffect(currentUser?.uid) {
        val ownerId = currentUser?.uid

        val listener = if (ownerId != null) {
            tagRepository.getTags(
                ownerId = ownerId,
                onSuccess = { savedTags ->
                    tags.clear()
                    tags.addAll(savedTags)
                },
                onError = { exception ->
                    Toast.makeText(
                        context,
                        "Error al cargar etiquetas: ${exception.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        } else {
            null
        }

        onDispose {
            listener?.remove()
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf("") }

    val normalizedTag = newTag.trim()
    val tagAlreadyExists = tags.any {
        it.equals(normalizedTag, ignoreCase = true)
    }
    val canAddTag = normalizedTag.isNotEmpty() && !tagAlreadyExists

    // Aquí continúa el resto de tu interfaz actual
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Etiquetas",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Organiza tus etiquetas para las listas de la compra.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Button(
            onClick = {
                newTag = ""
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir etiqueta")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            items(
                items = tags,
                key = { tag -> tag }
            ) { tag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                HorizontalDivider()
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text("Nueva etiqueta")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTag,
                        onValueChange = { newTag = it },
                        label = {
                            Text("Nombre de la etiqueta")
                        },
                        singleLine = true,
                        supportingText = {
                            if (tagAlreadyExists) {
                                Text("Esta etiqueta ya existe")
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = canAddTag,
                    onClick = {
                        val ownerId = currentUser?.uid

                        if (ownerId == null) {
                            Toast.makeText(
                                context,
                                "Debes iniciar sesión para crear una etiqueta",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            tagRepository.createTag(
                                name = normalizedTag,
                                ownerId = ownerId,
                                onSuccess = {
                                    newTag = ""
                                    showDialog = false

                                    Toast.makeText(
                                        context,
                                        "Etiqueta guardada correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onError = { exception ->
                                    Toast.makeText(
                                        context,
                                        "Error al guardar: ${exception.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    }
                ) {
                    Text("Añadir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        newTag = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
