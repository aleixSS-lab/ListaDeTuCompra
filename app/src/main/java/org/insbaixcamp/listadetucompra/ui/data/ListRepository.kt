
package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList

class ListRepository {

    private val db = FirebaseFirestore.getInstance()

    // Guarda una lista nueva en Firestore.
    fun createList(
        name: String,
        ownerId: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val list = ShoppingList(
            name = name,
            ownerId = ownerId,
            member = listOf(ownerId),
            createdAt = System.currentTimeMillis()
        )

        db.collection("list")
            .add(list)
            .addOnSuccessListener { document ->
                onSuccess(document.id)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    // Escucha las listas del usuario y sus cambios.
    fun getLists(
        ownerId: String,
        onSuccess: (List<ShoppingList>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {

        return db.collection("list")
            .whereEqualTo("ownerId", ownerId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val lists = snapshot.documents.mapNotNull { document ->
                        document.toObject(ShoppingList::class.java)
                            ?.copy(id = document.id)
                    }

                    onSuccess(lists)
                }
            }
    }
}
