package org.insbaixcamp.listadetucompra.ui.screen

import android.net.Uri

// Rutas de navegación

object Routes {
    const val HOME = "main"
    const val TAGS = "labels"
    const val LIST = "list/{listId}?name={listName}"

    fun list(listId: String, name: String = ""): String {
        val encodedName = Uri.encode(name)
        return "list/$listId?name=$encodedName"
    }
}
