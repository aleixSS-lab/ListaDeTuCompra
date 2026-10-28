package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp


//Clase de la navegacion
data class NavItem(
    val route: String, //Ruta
    val label: String, //Texto debajo del icono
    val icon: String //Icono
)

//Lista de NavItems --> para luego hacer un bucle y no tener que reutilizar codigo
val navItems = listOf(
    NavItem(Routes.MAIN, "Inicio", "🏠"),
    NavItem(Routes.LABELS, "Etiquetas", "🏷️")
)

@Composable
fun BottomBar(
    rutaActual : String?, //Ruta actual
    onItemClick : (String) -> Unit //Metodo que al click se le pasa la rutaActual
){
    NavigationBar{
        navItems.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.route,
                onClick = { onItemClick(item.route) },
                icon = { Text(text = item.icon, fontSize = 22.sp) },
                label = { Text(text = item.label) }
        )
        }
    }
}