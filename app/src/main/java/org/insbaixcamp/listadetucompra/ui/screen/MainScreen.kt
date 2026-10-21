package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreenContent(
    userDisplayName: String = "Usuario",
    onSignOut: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            pantallaMenuNavegacion(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            pantallaTop(
                userDisplayName = userDisplayName,
                onSignOut = onSignOut
            )
            Spacer(modifier = Modifier.height(16.dp))
            pantallaMedio()
        }
    }
}

@Preview
@Composable
fun pantallaInicial() {
    MainScreenContent()
}

@Composable
fun pantallaTop(
    userDisplayName: String = "Usuario",
    onSignOut: (() -> Unit)? = null
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ListaDeTuCompra",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hola, $userDisplayName",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (onSignOut != null) {
                OutlinedButton(onClick = onSignOut) {
                    Text(text = "Salir", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 4.dp)
                    .border(1.dp, color = Color.Black, shape = CircleShape),
                enabled = true,
                onClick = {
                    // Aqui llamamos al metodo añadir lista
                }
            ) {
                Text(text = " + ")
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 4.dp)
                    .border(1.dp, color = Color.Black, shape = CircleShape),
                enabled = true,
                onClick = {
                    // Aqui llamamos al metodo eliminar lista
                }
            ) {
                Text(text = " - ")
            }
        }
    }
}

@Composable
fun pantallaMedio() {

}

@Composable
fun pantallaMenuNavegacion(
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = {
                onTabSelected(0)
                // Aqui se llama a la pagina Home
            },
            icon = { Text(text = "🏠", fontSize = 20.sp) },
            label = { Text(text = "Inicio") }
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = {
                onTabSelected(1)
                // Aqui se llama a la pagina de etiquetas
            },
            icon = { Text(text = "🏷️", fontSize = 20.sp) },
            label = { Text(text = "Etiquetas") }
        )
    }
}
