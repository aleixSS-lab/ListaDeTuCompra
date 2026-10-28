package org.insbaixcamp.listadetucompra.ui.model

data class ShoppingList(
    val id : String = "",  //ID LISTA
    val name : String = "", // NAME LISTA
    val ownerId: String = "", // ID OWNER LISTA
    val member: List<String> = emptyList(), // ID MEMBER LISTA
    val createdAt : Long  = 0L, // FECHA DE CREACION EN MILISEGUNDOS
    val purchasedAt: Long? = null // FECHA DE COMPRA , NULL = NO COMPRADA
)

