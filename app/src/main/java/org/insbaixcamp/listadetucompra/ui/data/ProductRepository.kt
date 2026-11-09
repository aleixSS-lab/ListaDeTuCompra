
package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.insbaixcamp.listadetucompra.ui.model.ShoppingProduct

class ProductRepository {

    private val db = FirebaseFirestore.getInstance()

    private fun productsCollection(listId: String) =
        db.collection("list")
            .document(listId)
            .collection("products")

    fun getProducts(
        listId: String,
        onSuccess: (List<ShoppingProduct>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return productsCollection(listId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { document ->
                        document.toObject(ShoppingProduct::class.java)
                            ?.copy(id = document.id)
                    }.sortedBy { it.createdAt }

                    onSuccess(products)
                }
            }
    }

    fun addProduct(
        listId: String,
        name: String,
        tagId: String = "",
        tagName: String = "",
        categoryId: String = "",
        categoryName: String = "",
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val product = ShoppingProduct(
            name = name.trim(),
            completed = false,
            createdAt = System.currentTimeMillis(),
            tagId = tagId,
            tagName = tagName,
            categoryId = categoryId,
            categoryName = categoryName
        )

        productsCollection(listId)
            .add(product)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun updateProduct(
        listId: String,
        productId: String,
        completed: Boolean,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        productsCollection(listId)
            .document(productId)
            .update("completed", completed)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun updateProductDetails(
        listId: String,
        productId: String,
        name: String,
        categoryId: String,
        categoryName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val cleanName = name.trim()
        if (cleanName.isBlank()) {
            onError(IllegalArgumentException("El nombre del producto no puede estar vacío."))
            return
        }

        productsCollection(listId).document(productId)
            .update(
                mapOf(
                    "name" to cleanName,
                    "categoryId" to categoryId,
                    "categoryName" to categoryName
                )
            )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun updateProductTag(
        listId: String,
        productId: String,
        tagId: String,
        tagName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        productsCollection(listId)
            .document(productId)
            .update(
                mapOf(
                    "tagId" to tagId,
                    "tagName" to tagName
                )
            )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun deleteProduct(
        listId: String,
        productId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        productsCollection(listId)
            .document(productId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }
}
