package org.insbaixcamp.listadetucompra.ui.model

data class ShoppingList(
    val id : String = "",
    val name : String = "",
    val ownerId: String = "",
    val member: List<String> = emptyList()
)

