package org.insbaixcamp.listadetucompra.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Etiquetas",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Organiza tus etiquetas para las listas de la compra.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Button(
            onClick = {
                newTag = ""
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Añadir etiqueta",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = tags,
                key = { tag -> tag }
            ) { tag ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(
                    text = "Nueva etiqueta",
                    fontWeight = FontWeight.Bold
                )
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
                        shape = RoundedCornerShape(12.dp),
                        supportingText = {
                            if (tagAlreadyExists) {
                                Text(
                                    text = "Esta etiqueta ya existe",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = canAddTag,
                    shape = RoundedCornerShape(12.dp),
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
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
