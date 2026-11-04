package org.insbaixcamp.listadetucompra.ui.model

data class ShoppingProduct(

    val id: String = "",
    val name: String = "",
    val completed: Boolean = false,
    val createdAt: Long = 0L,
    val tagId: String = "",
    val tagName: String = ""
)

