
package org.insbaixcamp.listadetucompra.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.insbaixcamp.listadetucompra.ui.screen.Routes

@Composable
fun BottomBar(
    selectedRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedRoute == Routes.HOME,
            onClick = {
                onNavigate(Routes.HOME)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Casa"
                )
            },
            label = {
                Text("Casa")
            }
        )

        NavigationBarItem(
            selected = selectedRoute == Routes.TAGS,
            onClick = {
                onNavigate(Routes.TAGS)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.LocalOffer,
                    contentDescription = "Etiquetas"
                )
            },
            label = {
                Text("Etiquetas")
            }
        )
    }
}
