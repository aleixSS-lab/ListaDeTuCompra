
package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList

class ListRepository {

    private val db = FirebaseFirestore.getInstance()

    // Crear una lista nueva.
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

    // Recuperar las listas propias y las compartidas.
    fun getLists(
        userId: String,
        onSuccess: (List<ShoppingList>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return db.collection("list")
            .whereArrayContains("member", userId)
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

    // Añadir un usuario como miembro de una lista.
    fun shareList(
        listId: String,
        userId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("list")
            .document(listId)
            .update("member", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }



    // Buscar un usuario por su correo electrónico.
    fun findUserByEmail(
        email: String,
        onSuccess: (String?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("users")
            .whereEqualTo("email", email.trim().lowercase())
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val userId = snapshot.documents
                    .firstOrNull()
                    ?.getString("uid")

                onSuccess(userId)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}
