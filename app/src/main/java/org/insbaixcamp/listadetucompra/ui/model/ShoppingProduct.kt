package org.insbaixcamp.listadetucompra.ui.model

data class ShoppingProduct(
    val id: String = "",
    val name: String = "",
    val completed: Boolean = false,
    val createdAt: Long = 0L,
    // Etiquetas personales (característica extra existente).
    val tagId: String = "",
    val tagName: String = "",
    // Categoría propia de la lista, compartida con todos sus miembros.
    val categoryId: String = "",
    val categoryName: String = ""
)
