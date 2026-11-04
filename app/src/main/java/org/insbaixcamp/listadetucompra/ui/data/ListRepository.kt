package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList

data class ShoppingProduct(
    val id: String = "",
    val name: String = "",
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

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

    // Elimina una lista completa por su ID.
    fun deleteList(
        listId: String,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        db.collection("list")
            .document(listId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // Escucha las listas del usuario y las ordena con la más reciente primero.
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
                    }.sortedByDescending { it.createdAt }

                    onSuccess(lists)
                }
            }
    }

    // Escucha en tiempo real los productos de una lista específica.
    fun getProducts(
        listId: String,
        onSuccess: (List<ShoppingProduct>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return db.collection("list")
            .document(listId)
            .collection("products")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ShoppingProduct::class.java)?.copy(id = doc.id)
                    }
                    onSuccess(products)
                }
            }
    }

    // Añade un producto a la subcolección 'products' de la lista.
    fun addProduct(
        listId: String,
        name: String,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        val product = ShoppingProduct(
            name = name,
            completed = false,
            createdAt = System.currentTimeMillis()
        )

        db.collection("list")
            .document(listId)
            .collection("products")
            .add(product)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // Cambia el estado de completado de un producto.
    fun toggleProductCompleted(
        listId: String,
        productId: String,
        completed: Boolean,
        onError: (Exception) -> Unit = {}
    ) {
        db.collection("list")
            .document(listId)
            .collection("products")
            .document(productId)
            .update("completed", completed)
            .addOnFailureListener { onError(it) }
    }

    // Elimina un producto de la lista.
    fun deleteProduct(
        listId: String,
        productId: String,
        onError: (Exception) -> Unit = {}
    ) {
        db.collection("list")
            .document(listId)
            .collection("products")
            .document(productId)
            .delete()
            .addOnFailureListener { onError(it) }
    }
}
