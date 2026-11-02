package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.insbaixcamp.listadetucompra.ui.auth.AuthUiState
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.insbaixcamp.listadetucompra.ui.data.ListRepository

private data class ShoppingProduct(
    val id: Long,
    val name: String,
    val completed: Boolean = false
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    listId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val products = remember(listId) {
        mutableStateListOf<ShoppingProduct>()
    }
    val repository = remember { ListRepository() }
    val scope = rememberCoroutineScope()

    var showShareDialog by remember { mutableStateOf(false) }
    var shareEmail by remember { mutableStateOf("") }
    var isSharing by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi lista de la compra",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            shareEmail = ""
                            showShareDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir lista"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    productName = ""
                    showAddDialog = true
                }
            ) {
                Text("+ Añadir producto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista: $listId",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${products.count { it.completed }} de ${products.size} productos comprados",
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            if (products.isEmpty()) {
                Text(
                    text = "Todavía no hay productos. Pulsa + Añadir producto para empezar.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = products,
                        key = { it.id }
                    ) { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = product.completed,
                                onCheckedChange = { checked ->
                                    val index = products.indexOfFirst {
                                        it.id == product.id
                                    }
                                    if (index >= 0) {
                                        products[index] = product.copy(
                                            completed = checked
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = product.name,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                textDecoration = if (product.completed) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            )

                            TextButton(
                                onClick = {
                                    products.removeAll { it.id == product.id }
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }


    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isSharing) {
                    showShareDialog = false
                }
            },
            title = {
                Text("Compartir lista")
            },
            text = {
                OutlinedTextField(
                    value = shareEmail,
                    onValueChange = { shareEmail = it },
                    label = { Text("Correo electrónico") },
                    placeholder = { Text("usuario@gmail.com") },
                    singleLine = true,
                    enabled = !isSharing
                )
            },
            confirmButton = {
                Button(
                    enabled = shareEmail.isNotBlank() && !isSharing,
                    onClick = {
                        val email = shareEmail.trim().lowercase()

                        if (email.isNotBlank()) {
                            isSharing = true

                            repository.findUserByEmail(
                                email = email,
                                onSuccess = { userId ->
                                    if (userId == null) {
                                        isSharing = false
                                        Toast.makeText(
                                            context,
                                            "No existe ningún usuario con ese correo.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        repository.shareList(
                                            listId = listId,
                                            userId = userId,
                                            onSuccess = {
                                                isSharing = false
                                                showShareDialog = false
                                                Toast.makeText(
                                                    context,
                                                    "Lista compartida correctamente.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            },
                                            onError = { exception ->
                                                isSharing = false
                                                Toast.makeText(
                                                    context,
                                                    "Error al compartir: ${exception.localizedMessage}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        )
                                    }
                                },
                                onError = { exception ->
                                    isSharing = false
                                    Toast.makeText(
                                        context,
                                        "Error al buscar usuario: ${exception.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    }
                ) {
                    Text(if (isSharing) "Compartiendo..." else "Compartir")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isSharing,
                    onClick = {
                        showShareDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
0

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
            },
            title = {
                Text("Añadir producto")
            },
            text = {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nombre del producto") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val name = productName.trim()

                        if (name.isNotEmpty()) {
                            products.add(
                                ShoppingProduct(
                                    id = System.nanoTime(),
                                    name = name
                                )
                            )
                            showAddDialog = false
                        }
                    },
                    enabled = productName.isNotBlank()
                ) {
                    Text("Añadir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
@Preview
@Composable
fun ShoppingListScreen() {
    ShoppingListScreen(
        listId = "list1",
        onBack = {}
    )
}