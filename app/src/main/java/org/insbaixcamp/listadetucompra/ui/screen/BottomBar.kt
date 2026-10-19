package org.insbaixcamp.listadetucompra.ui.screen

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