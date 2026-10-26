package org.insbaixcamp.listadetucompra.ui.screen

// Rutas de navegación

object Routes{
    const val HOME = "main"
    const val TAGS = "labels"
    const val LIST = "list/{listId}"

    fun list(listId: String) = "list/$listId"
}