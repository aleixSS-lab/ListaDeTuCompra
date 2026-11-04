
package org.insbaixcamp.listadetucompra.ui.screen

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import org.insbaixcamp.listadetucompra.ui.data.ListRepository
import org.insbaixcamp.listadetucompra.ui.data.ProductRepository
import org.insbaixcamp.listadetucompra.ui.model.ShoppingProduct

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.LaunchedEffect
import org.insbaixcamp.listadetucompra.ui.data.TagRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    listId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val listRepository = remember { ListRepository() }
    val productRepository = remember { ProductRepository() }

    val tagRepository = remember { TagRepository() }

    val tags = remember {
        mutableStateListOf<String>()
    }

    var selectedTag by remember {
        mutableStateOf("")
    }

    var tagMenuExpanded by remember {
        mutableStateOf(false)
    }


    var showEditTagDialog by remember {
        mutableStateOf(false)
    }

    var editingProduct by remember {
        mutableStateOf<ShoppingProduct?>(null)
    }

    var editingTag by remember {
        mutableStateOf("")
    }

    var editTagMenuExpanded by remember {
        mutableStateOf(false)
    }

    val products = remember(listId) {
        mutableStateListOf<ShoppingProduct>()
    }

    var showShareDialog by remember { mutableStateOf(false) }
    var shareEmail by remember { mutableStateOf("") }
    var isSharing by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }

    var listName by remember { mutableStateOf("Mi lista") }
    var sharedEmails by remember { mutableStateOf<List<String>>(emptyList()) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }


    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid




    DisposableEffect(listId) {
        val listener = productRepository.getProducts(
            listId = listId,
            onSuccess = { savedProducts ->
                products.clear()
                products.addAll(savedProducts)

                Toast.makeText(
                    context,
                    "Productos recibidos: ${savedProducts.size}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onError = { exception ->
                Toast.makeText(
                    context,
                    "Error al cargar productos: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        onDispose {
            listener.remove()
        }
    }

    DisposableEffect(listId) {
        val listener = ListRepository().getListName(
            listId = listId,
            onSuccess = { name ->
                listName = name
            },
            onError = { exception ->
                Toast.makeText(
                    context,
                    "Error al cargar el nombre: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        onDispose {
            listener.remove()

        }
    }

    LaunchedEffect(listId) {
        listRepository.getSharedUserEmails(
            listId = listId,
            onSuccess = { emails ->
                sharedEmails = emails
            },
            onError = { exception ->
                Toast.makeText(
                    context,
                    "Error al cargar usuarios: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
    DisposableEffect(currentUserId) {
        if (currentUserId == null) {
            onDispose { }
        } else {
            val defaultTagsListener = tagRepository.getDefaultTags(
                onSuccess = { defaultTags ->
                    tags.clear()
                    tags.addAll(defaultTags)
                },
                onError = { exception ->
                    Toast.makeText(
                        context,
                        "Error al cargar etiquetas predeterminadas: ${exception.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            val personalTagsListener = tagRepository.getTags(
                ownerId = currentUserId,
                onSuccess = { personalTags ->
                    tags.addAll(personalTags.filterNot { it in tags })
                },
                onError = { exception ->
                    Toast.makeText(
                        context,
                        "Error al cargar etiquetas personales: ${exception.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            onDispose {
                defaultTagsListener.remove()
                personalTagsListener.remove()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = listName,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Volver")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            newListName = listName
                            showRenameDialog = true
                        }
                    ) {
                        Text("✏️")
                    }
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
                text = if (sharedEmails.isEmpty()) {
                    "No compartida"
                } else {
                    "Compartida con: ${sharedEmails.joinToString(", ")}"
                },
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
                                    productRepository.updateProduct(
                                        listId = listId,
                                        productId = product.id,
                                        completed = checked,
                                        onSuccess = {},
                                        onError = { exception ->
                                            Toast.makeText(
                                                context,
                                                "Error al actualizar producto: ${exception.localizedMessage}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))


                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textDecoration = if (product.completed) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    }
                                )

                                Text(
                                    text = if (product.tagName.isBlank()) {
                                        "Sin etiqueta"
                                    } else {
                                        product.tagName
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }


                            TextButton(
                                onClick = {
                                    editingProduct = product
                                    editingTag = product.tagName
                                    showEditTagDialog = true
                                }
                            ) {
                                Text("Etiqueta")
                            }


                            TextButton(
                                onClick = {
                                    productRepository.deleteProduct(
                                        listId = listId,
                                        productId = product.id,
                                        onSuccess = {},
                                        onError = { exception ->
                                            Toast.makeText(
                                                context,
                                                "Error al eliminar producto: ${exception.localizedMessage}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    )
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
                if (!isSharing) showShareDialog = false
            },
            title = { Text("Compartir lista") },
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
                        isSharing = true

                        listRepository.findUserByEmail(
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
                                    listRepository.shareList(
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
                ) {
                    Text(if (isSharing) "Compartiendo..." else "Compartir")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isSharing,
                    onClick = { showShareDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Añadir producto") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Nombre del producto") },
                        singleLine = true
                    )

                    TextButton(
                        onClick = {
                            tagMenuExpanded = true
                        }
                    ) {
                        Text(
                            if (selectedTag.isBlank()) {
                                "Seleccionar etiqueta (opcional)"
                            } else {
                                "Etiqueta: $selectedTag"
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = tagMenuExpanded,
                        onDismissRequest = {
                            tagMenuExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin etiqueta") },
                            onClick = {
                                selectedTag = ""
                                tagMenuExpanded = false
                            }
                        )

                        tags.distinct().forEach { tag ->
                            DropdownMenuItem(
                                text = { Text(tag) },
                                onClick = {
                                    selectedTag = tag
                                    tagMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = productName.isNotBlank(),
                    onClick = {
                        val name = productName.trim()

                        if (name.isNotEmpty()) {


                            productRepository.addProduct(
                                listId = listId,
                                name = name,
                                tagName = selectedTag,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Producto guardado correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    productName = ""
                                    selectedTag = ""
                                    showAddDialog = false
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
                        showAddDialog = false
                        productName = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showEditTagDialog && editingProduct != null) {
        AlertDialog(
            onDismissRequest = {
                showEditTagDialog = false
                editingProduct = null
            },
            title = {
                Text("Cambiar etiqueta")
            },
            text = {
                Column {
                    Text(
                        text = "Producto: ${editingProduct!!.name}",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextButton(
                        onClick = {
                            editTagMenuExpanded = true
                        }
                    ) {
                        Text(
                            if (editingTag.isBlank()) {
                                "Seleccionar etiqueta"
                            } else {
                                "Etiqueta: $editingTag"
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = editTagMenuExpanded,
                        onDismissRequest = {
                            editTagMenuExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Sin etiqueta")
                            },
                            onClick = {
                                editingTag = ""
                                editTagMenuExpanded = false
                            }
                        )

                        tags.distinct().forEach { tag ->
                            DropdownMenuItem(
                                text = {
                                    Text(tag)
                                },
                                onClick = {
                                    editingTag = tag
                                    editTagMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val product = editingProduct

                        if (product != null) {
                            productRepository.updateProductTag(
                                listId = listId,
                                productId = product.id,
                                tagId = "",
                                tagName = editingTag,
                                onSuccess = {
                                    showEditTagDialog = false
                                    editingProduct = null

                                    Toast.makeText(
                                        context,
                                        "Etiqueta actualizada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onError = { exception ->
                                    Toast.makeText(
                                        context,
                                        "Error al actualizar etiqueta: ${exception.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditTagDialog = false
                        editingProduct = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = {
                showRenameDialog = false
            },
            title = {
                Text("Cambiar nombre de la lista")
            },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("Nombre de la lista") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    enabled = newListName.isNotBlank(),
                    onClick = {
                        listRepository.renameList(
                            listId = listId,
                            newName = newListName,
                            onSuccess = {
                                showRenameDialog = false
                                Toast.makeText(
                                    context,
                                    "Nombre actualizado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = { exception ->
                                Toast.makeText(
                                    context,
                                    "Error al cambiar el nombre: ${exception.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRenameDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


}
