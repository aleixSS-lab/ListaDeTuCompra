package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de ejemplo.
// Si ya tienes un modelo de lista, utiliza el existente.
data class ShoppingListUi(
    val id: String,
    val name: String
)

@Composable
fun MainScreen(
    lists: List<ShoppingListUi>,
    onAddList: (String) -> Unit,
    onOpenList: (ShoppingListUi) -> Unit
) {
    var showCreateDialog by remember {
        mutableStateOf(false)
    }

    var newListName by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Tu lista de la compra",
            fontSize = 27.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 16.dp
            )
        ) {
            // Botón para crear una lista.
            item(key = "add_list") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .clickable {
                            newListName = ""
                            showCreateDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }
            }

            // Una casilla por cada lista existente.
            items(
                items = lists,
                key = { it.id }
            ) { shoppingList ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .clickable {
                            onOpenList(shoppingList)
                        }
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = shoppingList.name,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 3
                    )
                }
            }
        }
    }

    // Diálogo para crear una lista nueva.
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
            },
            title = {
                Text("Crear nueva lista")
            },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = {
                        newListName = it
                    },
                    label = {
                        Text("Nombre de la lista")
                    },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    enabled = newListName.isNotBlank(),
                    onClick = {
                        val name = newListName.trim()

                        if (name.isNotEmpty()) {
                            onAddList(name)
                            showCreateDialog = false
                        }
                    }
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
