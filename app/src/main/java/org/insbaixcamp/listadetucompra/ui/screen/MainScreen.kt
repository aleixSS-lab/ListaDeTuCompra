
package org.insbaixcamp.listadetucompra.ui.screen




import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight


import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import org.insbaixcamp.listadetucompra.ui.data.ListRepository
import androidx.compose.runtime.DisposableEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onListClick: (String) -> Unit,
    onTagsClick: () -> Unit
) {

    val listRepository = remember { ListRepository() }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    val lists = remember {
        mutableStateListOf<ShoppingList>()
    }

    DisposableEffect(currentUser?.uid) {
        val ownerId = currentUser?.uid

        val listener = if (ownerId != null) {
            listRepository.getLists(
                ownerId = ownerId,
                onSuccess = { firestoreLists ->
                    lists.clear()
                    lists.addAll(firestoreLists)
                },
                onError = { exception ->
                    Toast.makeText(
                        context,
                        "Error al cargar las listas: ${exception.localizedMessage}",
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


    var showCreateDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
            }
        },
        floatingActionButton = {
            IconButton(
                onClick = {
                    newListName = ""
                    showCreateDialog = true
                },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear lista",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
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
                text = "Mis listas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
//            Generar las tarjetas automaticamente
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = lists,
                    key = { list -> list.id }
                ) { shoppingList ->

                    ListCard(
                        list = shoppingList,
                        onClick = {
                            onListClick(shoppingList.id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
//Crear lista (ABRE UN POPUP)
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
            },
            title = {
                Text("Crear lista")
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

                    onClick = {
                        val name = newListName.trim()
                        val ownerId = currentUser?.uid

                        if (name.isNotEmpty() && ownerId != null) {
                            listRepository.createList(
                                name = name,
                                ownerId = ownerId,

                                onSuccess = { listId ->
                                    showCreateDialog = false
                                    newListName = ""

                                    Toast.makeText(
                                        context,
                                        "Lista guardada correctamente",
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
                        } else if (ownerId == null) {
                            Toast.makeText(
                                context,
                                "Debes iniciar sesión para crear una lista",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },

                    enabled = newListName.isNotBlank()
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onListClick = {},
        onTagsClick = {}
    )
}
