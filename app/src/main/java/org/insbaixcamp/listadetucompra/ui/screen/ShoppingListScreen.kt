package org.insbaixcamp.listadetucompra.ui.screen

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.LaunchedEffect
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
import org.insbaixcamp.listadetucompra.ui.data.Catalog
import org.insbaixcamp.listadetucompra.ui.data.CategoryRepository
import org.insbaixcamp.listadetucompra.ui.data.ListRepository
import org.insbaixcamp.listadetucompra.ui.data.ProductRepository
import org.insbaixcamp.listadetucompra.ui.data.TagRepository
import org.insbaixcamp.listadetucompra.ui.model.ShoppingCategory
import org.insbaixcamp.listadetucompra.ui.model.ShoppingProduct
import org.insbaixcamp.listadetucompra.ui.theme.AppLanguage
import org.insbaixcamp.listadetucompra.ui.theme.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    listId: String,
    listName: String = "",
    language: AppLanguage = AppLanguage.ES,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val listRepository = remember { ListRepository() }
    val productRepository = remember { ProductRepository() }
    val categoryRepository = remember { CategoryRepository() }
    val tagRepository = remember { TagRepository() }

    val products = remember(listId) { mutableStateListOf<ShoppingProduct>() }
    val categories = remember(listId) { mutableStateListOf<ShoppingCategory>() }
    val defaultTags = remember { mutableStateListOf<String>() }
    val personalTags = remember { mutableStateListOf<String>() }
    val tags = (defaultTags + personalTags).distinctBy { it.trim().lowercase() }

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var currentListName by remember(listId, listName) {
        mutableStateOf(listName.ifBlank { "Mi lista" })
    }
    var sharedEmails by remember { mutableStateOf<List<String>>(emptyList()) }

    // Compartir y renombrar lista.
    var showShareDialog by remember { mutableStateOf(false) }
    var shareEmail by remember { mutableStateOf("") }
    var isSharing by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }

    // Crear producto.
    var showAddDialog by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }
    var selectedCategoryId by remember(listId) { mutableStateOf("") }
    var addCategoryMenuExpanded by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf("") }
    var addTagMenuExpanded by remember { mutableStateOf(false) }

    // Editar producto.
    var showEditProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ShoppingProduct?>(null) }
    var editedProductName by remember { mutableStateOf("") }
    var editedProductCategoryId by remember { mutableStateOf("") }
    var editProductCategoryMenuExpanded by remember { mutableStateOf(false) }

    // Etiquetas extra existentes.
    var showEditTagDialog by remember { mutableStateOf(false) }
    var editingTag by remember { mutableStateOf("") }
    var editTagMenuExpanded by remember { mutableStateOf(false) }

    // Gestión de categorías de esta lista.
    var showCategoriesDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var categoryBeingEdited by remember { mutableStateOf<ShoppingCategory?>(null) }
    var editedCategoryName by remember { mutableStateOf("") }
    var categoryToDelete by remember { mutableStateOf<ShoppingCategory?>(null) }
    var selectedCategoryFilter by remember(listId) { mutableStateOf<String?>(null) }

    fun categoryName(categoryId: String): String {
        if (categoryId.isBlank()) return ""
        return categories.firstOrNull { it.id == categoryId }?.name.orEmpty()
    }

    fun showError(prefix: String, exception: Exception) {
        Toast.makeText(
            context,
            "$prefix: ${exception.localizedMessage ?: "Error desconocido"}",
            Toast.LENGTH_LONG
        ).show()
    }

    // Escucha de productos compartidos en tiempo real.
    DisposableEffect(listId) {
        val listener = productRepository.getProducts(
            listId = listId,
            onSuccess = { savedProducts ->
                products.clear()
                products.addAll(savedProducts)
            },
            onError = { exception -> showError("Error al cargar productos", exception) }
        )
        onDispose { listener.remove() }
    }

    // Escucha el nombre de la lista.
    DisposableEffect(listId) {
        val listener = listRepository.getListName(
            listId = listId,
            onSuccess = { currentListName = it },
            onError = { exception -> showError("Error al cargar el nombre", exception) }
        )
        onDispose { listener.remove() }
    }

    // Escucha las categorías compartidas y prepara las iniciales para listas antiguas.
    DisposableEffect(listId) {
        val listener = categoryRepository.getCategories(
            listId = listId,
            onSuccess = { savedCategories ->
                categories.clear()
                categories.addAll(savedCategories)
                if (selectedCategoryFilter != null && savedCategories.none { it.id == selectedCategoryFilter }) {
                    selectedCategoryFilter = null
                }
            },
            onError = { exception -> showError("Error al cargar categorías", exception) }
        )
        categoryRepository.ensureDefaultCategories(
            listId = listId,
            defaultNames = Catalog.defaultCategories,
            onError = { exception -> showError("Error al preparar categorías", exception) }
        )
        onDispose { listener.remove() }
    }

    // Carga de las etiquetas personales y predeterminadas.
    DisposableEffect(currentUserId) {
        if (currentUserId == null) {
            onDispose { }
        } else {
            val defaultListener = tagRepository.getDefaultTags(
                onSuccess = { savedTags ->
                    defaultTags.clear()
                    defaultTags.addAll(savedTags)
                },
                onError = { exception -> showError("Error al cargar etiquetas predeterminadas", exception) }
            )
            val personalListener = tagRepository.getTags(
                ownerId = currentUserId,
                onSuccess = { savedTags ->
                    personalTags.clear()
                    personalTags.addAll(savedTags)
                },
                onError = { exception -> showError("Error al cargar etiquetas personales", exception) }
            )
            onDispose {
                defaultListener.remove()
                personalListener.remove()
            }
        }
    }

    // Los miembros pueden ver quién más tiene acceso. Se recarga después de compartir.
    LaunchedEffect(listId) {
        listRepository.getSharedUserEmails(
            listId = listId,
            onSuccess = { sharedEmails = it },
            onError = { exception -> showError("Error al cargar usuarios", exception) }
        )
    }

    val visibleProducts = products.filter { product ->
        selectedCategoryFilter == null || product.categoryId == selectedCategoryFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = currentListName, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Volver") }
                },
                actions = {
                    IconButton(
                        onClick = {
                            newListName = currentListName
                            showRenameDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = Strings.renameListButton(language)
                        )
                    }
                    TextButton(
                        onClick = {
                            shareEmail = ""
                            showShareDialog = true
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(Strings.shareListButton(language))
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    productName = ""
                    selectedCategoryId = ""
                    selectedTag = ""
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
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Categorías", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = { showCategoriesDialog = true }) {
                    Icon(Icons.Default.Category, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Gestionar")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategoryFilter == null,
                    onClick = { selectedCategoryFilter = null },
                    label = { Text("Todas") }
                )
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategoryFilter == category.id,
                        onClick = { selectedCategoryFilter = category.id },
                        label = { Text(category.name) }
                    )
                }
                FilterChip(
                    selected = selectedCategoryFilter == "__uncategorized__",
                    onClick = { selectedCategoryFilter = "__uncategorized__" },
                    label = { Text("Sin categoría") }
                )
            }

            val filteredProducts = if (selectedCategoryFilter == "__uncategorized__") {
                products.filter { it.categoryId.isBlank() }
            } else {
                visibleProducts
            }

            if (filteredProducts.isEmpty()) {
                Text(
                    text = if (products.isEmpty()) {
                        "Todavía no hay productos. Pulsa + Añadir producto para empezar."
                    } else {
                        "No hay productos en esta categoría."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = filteredProducts, key = { it.id }) { product ->
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
                                        onError = { exception -> showError("Error al actualizar producto", exception) }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textDecoration = if (product.completed) TextDecoration.LineThrough else TextDecoration.None
                                )
                                val currentCategory = categoryName(product.categoryId).ifBlank {
                                    product.categoryName.ifBlank { "Sin categoría" }
                                }
                                Text(
                                    text = "$currentCategory · ${product.tagName.ifBlank { "Sin etiqueta" }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            IconButton(
                                onClick = {
                                    editingProduct = product
                                    editedProductName = product.name
                                    editedProductCategoryId = product.categoryId
                                    showEditProductDialog = true
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar producto")
                            }
                            IconButton(
                                onClick = {
                                    editingProduct = product
                                    editingTag = product.tagName
                                    showEditTagDialog = true
                                }
                            ) {
                                Icon(Icons.Default.LocalOffer, contentDescription = "Cambiar etiqueta")
                            }
                            IconButton(
                                onClick = {
                                    productRepository.deleteProduct(
                                        listId = listId,
                                        productId = product.id,
                                        onSuccess = {},
                                        onError = { exception -> showError("Error al eliminar producto", exception) }
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = "Eliminar producto",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Compartir la lista con otro usuario registrado.
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { if (!isSharing) showShareDialog = false },
            title = { Text("Compartir lista") },
            text = {
                Column {
                    OutlinedTextField(
                        value = shareEmail,
                        onValueChange = { shareEmail = it },
                        label = { Text("Correo electrónico") },
                        placeholder = { Text("usuario@gmail.com") },
                        singleLine = true,
                        enabled = !isSharing
                    )
                    if (sharedEmails.isNotEmpty()) {
                        Text(
                            text = "Compartida con: ${sharedEmails.joinToString(", ")}",
                            modifier = Modifier.padding(top = 10.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
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
                                    Toast.makeText(context, "No existe ningún usuario con ese correo.", Toast.LENGTH_LONG).show()
                                } else if (userId == currentUserId) {
                                    isSharing = false
                                    Toast.makeText(context, "Esta lista ya es tuya.", Toast.LENGTH_LONG).show()
                                } else {
                                    listRepository.shareList(
                                        listId = listId,
                                        userId = userId,
                                        onSuccess = {
                                            isSharing = false
                                            shareEmail = ""
                                            listRepository.getSharedUserEmails(
                                                listId = listId,
                                                onSuccess = { sharedEmails = it },
                                                onError = { exception -> showError("Error al actualizar miembros", exception) }
                                            )
                                            Toast.makeText(context, "Lista compartida correctamente.", Toast.LENGTH_SHORT).show()
                                        },
                                        onError = { exception ->
                                            isSharing = false
                                            showError("Error al compartir", exception)
                                        }
                                    )
                                }
                            },
                            onError = { exception ->
                                isSharing = false
                                showError("Error al buscar usuario", exception)
                            }
                        )
                    }
                ) { Text(if (isSharing) "Compartiendo..." else "Compartir") }
            },
            dismissButton = {
                TextButton(
                    enabled = !isSharing,
                    onClick = { showShareDialog = false }
                ) { Text("Cancelar") }
            }
        )
    }

    // Añadir producto: asigna automáticamente una categoría si el catálogo reconoce el nombre.
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Añadir producto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { newValue ->
                            productName = newValue
                            val suggestedName = Catalog.categoryFor(newValue)
                            if (suggestedName != null) {
                                categories.firstOrNull { it.name.equals(suggestedName, ignoreCase = true) }
                                    ?.let { selectedCategoryId = it.id }
                            }
                        },
                        label = { Text("Nombre del producto") },
                        singleLine = true
                    )
                    TextButton(onClick = { addCategoryMenuExpanded = true }) {
                        Text(
                            "Categoría: " + categories.firstOrNull { it.id == selectedCategoryId }?.name
                                .orEmpty().ifBlank { "Seleccionar categoría" }
                        )
                    }
                    DropdownMenu(
                        expanded = addCategoryMenuExpanded,
                        onDismissRequest = { addCategoryMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin categoría") },
                            onClick = {
                                selectedCategoryId = ""
                                addCategoryMenuExpanded = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    addCategoryMenuExpanded = false
                                }
                            )
                        }
                    }
                    TextButton(onClick = { addTagMenuExpanded = true }) {
                        Text(if (selectedTag.isBlank()) "Etiqueta (opcional)" else "Etiqueta: $selectedTag")
                    }
                    DropdownMenu(
                        expanded = addTagMenuExpanded,
                        onDismissRequest = { addTagMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin etiqueta") },
                            onClick = {
                                selectedTag = ""
                                addTagMenuExpanded = false
                            }
                        )
                        tags.distinct().forEach { tag ->
                            DropdownMenuItem(
                                text = { Text(tag) },
                                onClick = {
                                    selectedTag = tag
                                    addTagMenuExpanded = false
                                }
                            )
                        }
                    }
                    Text(
                        text = "Las etiquetas son opcionales y las categorías se comparten con todos los miembros de la lista.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = productName.isNotBlank(),
                    onClick = {
                        val chosenCategory = categories.firstOrNull { it.id == selectedCategoryId }
                        productRepository.addProduct(
                            listId = listId,
                            name = productName,
                            tagName = selectedTag,
                            categoryId = chosenCategory?.id.orEmpty(),
                            categoryName = chosenCategory?.name.orEmpty(),
                            onSuccess = {
                                productName = ""
                                selectedCategoryId = ""
                                selectedTag = ""
                                showAddDialog = false
                                Toast.makeText(context, "Producto guardado correctamente", Toast.LENGTH_SHORT).show()
                            },
                            onError = { exception -> showError("Error al guardar el producto", exception) }
                        )
                    }
                ) { Text("Añadir") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Editar nombre y categoría de un producto existente.
    if (showEditProductDialog && editingProduct != null) {
        AlertDialog(
            onDismissRequest = {
                showEditProductDialog = false
                editingProduct = null
            },
            title = { Text("Editar producto") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedProductName,
                        onValueChange = { editedProductName = it },
                        label = { Text("Nombre del producto") },
                        singleLine = true
                    )
                    TextButton(onClick = { editProductCategoryMenuExpanded = true }) {
                        Text(
                            "Categoría: " + categories.firstOrNull { it.id == editedProductCategoryId }?.name
                                .orEmpty().ifBlank { "Sin categoría" }
                        )
                    }
                    DropdownMenu(
                        expanded = editProductCategoryMenuExpanded,
                        onDismissRequest = { editProductCategoryMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin categoría") },
                            onClick = {
                                editedProductCategoryId = ""
                                editProductCategoryMenuExpanded = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    editedProductCategoryId = category.id
                                    editProductCategoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = editedProductName.isNotBlank(),
                    onClick = {
                        val product = editingProduct
                        val category = categories.firstOrNull { it.id == editedProductCategoryId }
                        if (product != null) {
                            productRepository.updateProductDetails(
                                listId = listId,
                                productId = product.id,
                                name = editedProductName,
                                categoryId = category?.id.orEmpty(),
                                categoryName = category?.name.orEmpty(),
                                onSuccess = {
                                    showEditProductDialog = false
                                    editingProduct = null
                                    Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                                },
                                onError = { exception -> showError("Error al editar producto", exception) }
                            )
                        }
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEditProductDialog = false
                    editingProduct = null
                }) { Text("Cancelar") }
            }
        )
    }

    // Cambiar etiqueta personal de un producto (característica extra conservada).
    if (showEditTagDialog && editingProduct != null) {
        AlertDialog(
            onDismissRequest = {
                showEditTagDialog = false
                editingProduct = null
            },
            title = { Text("Cambiar etiqueta") },
            text = {
                Column {
                    Text("Producto: ${editingProduct?.name}")
                    TextButton(onClick = { editTagMenuExpanded = true }) {
                        Text(if (editingTag.isBlank()) "Seleccionar etiqueta" else "Etiqueta: $editingTag")
                    }
                    DropdownMenu(
                        expanded = editTagMenuExpanded,
                        onDismissRequest = { editTagMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin etiqueta") },
                            onClick = {
                                editingTag = ""
                                editTagMenuExpanded = false
                            }
                        )
                        tags.distinct().forEach { tag ->
                            DropdownMenuItem(
                                text = { Text(tag) },
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
                        editingProduct?.let { product ->
                            productRepository.updateProductTag(
                                listId = listId,
                                productId = product.id,
                                tagId = "",
                                tagName = editingTag,
                                onSuccess = {
                                    showEditTagDialog = false
                                    editingProduct = null
                                    Toast.makeText(context, "Etiqueta actualizada", Toast.LENGTH_SHORT).show()
                                },
                                onError = { exception -> showError("Error al actualizar etiqueta", exception) }
                            )
                        }
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEditTagDialog = false
                    editingProduct = null
                }) { Text("Cancelar") }
            }
        )
    }

    // Crear, consultar, renombrar y eliminar categorías compartidas de la lista.
    if (showCategoriesDialog) {
        AlertDialog(
            onDismissRequest = { showCategoriesDialog = false },
            title = { Text("Gestionar categorías") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Nombre de la categoría") },
                        singleLine = true
                    )
                    Button(
                        enabled = newCategoryName.isNotBlank(),
                        onClick = {
                            val normalized = newCategoryName.trim()
                            if (categories.any { it.name.equals(normalized, ignoreCase = true) }) {
                                Toast.makeText(context, "Esta categoría ya existe", Toast.LENGTH_SHORT).show()
                            } else {
                                categoryRepository.createCategory(
                                    listId = listId,
                                    name = normalized,
                                    onSuccess = {
                                        newCategoryName = ""
                                        Toast.makeText(context, "Categoría creada", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { exception -> showError("Error al crear categoría", exception) }
                                )
                            }
                        }
                    ) { Text("Añadir categoría") }

                    Text("Categorías de esta lista", fontWeight = FontWeight.SemiBold)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(items = categories, key = { it.id }) { category ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(category.name, modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        categoryBeingEdited = category
                                        editedCategoryName = category.name
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar categoría")
                                }
                                IconButton(
                                    onClick = {
                                        if (categories.size <= 1) {
                                            Toast.makeText(context, "Debe quedar al menos una categoría", Toast.LENGTH_LONG).show()
                                        } else {
                                            categoryToDelete = category
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.DeleteOutline,
                                        contentDescription = "Eliminar categoría",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = "Las categorías pertenecen a esta lista y todos sus miembros pueden gestionarlas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoriesDialog = false }) { Text("Cerrar") }
            }
        )
    }

    if (categoryBeingEdited != null) {
        AlertDialog(
            onDismissRequest = { categoryBeingEdited = null },
            title = { Text("Editar categoría") },
            text = {
                OutlinedTextField(
                    value = editedCategoryName,
                    onValueChange = { editedCategoryName = it },
                    label = { Text("Nombre de la categoría") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    enabled = editedCategoryName.isNotBlank(),
                    onClick = {
                        val category = categoryBeingEdited
                        val cleanName = editedCategoryName.trim()
                        if (category != null) {
                            if (categories.any { it.id != category.id && it.name.equals(cleanName, ignoreCase = true) }) {
                                Toast.makeText(context, "Esta categoría ya existe", Toast.LENGTH_SHORT).show()
                            } else {
                                categoryRepository.renameCategory(
                                    listId = listId,
                                    categoryId = category.id,
                                    newName = cleanName,
                                    onSuccess = {
                                        categoryBeingEdited = null
                                        Toast.makeText(context, "Categoría actualizada", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { exception -> showError("Error al editar categoría", exception) }
                                )
                            }
                        }
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { categoryBeingEdited = null }) { Text("Cancelar") }
            }
        )
    }

    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            title = { Text("¿Eliminar categoría?") },
            text = {
                Text("Se eliminará '${categoryToDelete?.name}'. Los productos se conservarán, pero quedarán sin categoría.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        val category = categoryToDelete
                        if (category != null && categories.size > 1) {
                            categoryRepository.deleteCategory(
                                listId = listId,
                                categoryId = category.id,
                                onSuccess = {
                                    if (selectedCategoryFilter == category.id) selectedCategoryFilter = null
                                    categoryToDelete = null
                                    Toast.makeText(context, "Categoría eliminada", Toast.LENGTH_SHORT).show()
                                },
                                onError = { exception -> showError("Error al eliminar categoría", exception) }
                            )
                        } else {
                            categoryToDelete = null
                        }
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Cambiar nombre de la lista") },
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
                                Toast.makeText(context, "Nombre actualizado", Toast.LENGTH_SHORT).show()
                            },
                            onError = { exception -> showError("Error al cambiar el nombre", exception) }
                        )
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
