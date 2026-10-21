package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FirebaseFirestore
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList


class ListRepository {

    private val db = FirebaseFirestore.getInstance()

    fun createList(name : String, ownerId : String) {
        val list = ShoppingList(
            name = name,
            ownerId = ownerId,
            member = listOf(ownerId)
        )
        db.collection("list").add(list)
    }
}