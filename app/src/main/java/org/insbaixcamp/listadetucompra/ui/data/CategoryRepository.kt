package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.insbaixcamp.listadetucompra.ui.model.ShoppingCategory

/**
 * Categorías que pertenecen a una lista concreta.
 * Todos los miembros de la lista pueden consultarlas y modificarlas.
 */
class CategoryRepository {

    private val db = FirebaseFirestore.getInstance()

    private fun categoriesCollection(listId: String) =
        db.collection("list")
            .document(listId)
            .collection("categories")

    private fun productsCollection(listId: String) =
        db.collection("list")
            .document(listId)
            .collection("products")

    fun getCategories(
        listId: String,
        onSuccess: (List<ShoppingCategory>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return categoriesCollection(listId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val categories = snapshot.documents.mapNotNull { document ->
                        document.toObject(ShoppingCategory::class.java)
                            ?.copy(id = document.id)
                    }.sortedBy { it.name.lowercase() }

                    onSuccess(categories)
                }
            }
    }

    /**
     * Añade categorías iniciales solo cuando la lista todavía no tiene ninguna.
     * Los identificadores deterministas evitan duplicados si dos miembros abren
     * la lista a la vez por primera vez.
     */
    fun ensureDefaultCategories(
        listId: String,
        defaultNames: List<String>,
        onError: (Exception) -> Unit = {}
    ) {
        val collection = categoriesCollection(listId)
        collection.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty || defaultNames.isEmpty()) return@addOnSuccessListener

                val batch = db.batch()
                val now = System.currentTimeMillis()
                defaultNames.distinctBy { it.trim().lowercase() }.forEachIndexed { index, name ->
                    val categoryId = "default_$index"
                    val reference = collection.document(categoryId)
                    batch.set(
                        reference,
                        mapOf(
                            "name" to name.trim(),
                            "createdAt" to now
                        )
                    )
                }
                batch.commit().addOnFailureListener { exception -> onError(exception) }
            }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun createCategory(
        listId: String,
        name: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val cleanName = name.trim()
        if (cleanName.isBlank()) {
            onError(IllegalArgumentException("El nombre de la categoría no puede estar vacío."))
            return
        }

        val collection = categoriesCollection(listId)
        collection.get()
            .addOnSuccessListener { snapshot ->
                val duplicate = snapshot.documents.any {
                    it.getString("name")?.equals(cleanName, ignoreCase = true) == true
                }
                if (duplicate) {
                    onError(IllegalArgumentException("Esta categoría ya existe."))
                } else {
                    collection.add(
                        mapOf(
                            "name" to cleanName,
                            "createdAt" to System.currentTimeMillis()
                        )
                    )
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { exception -> onError(exception) }
                }
            }
            .addOnFailureListener { exception -> onError(exception) }
    }

    /** Renombra la categoría y mantiene sincronizado el texto guardado en productos. */
    fun renameCategory(
        listId: String,
        categoryId: String,
        newName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val cleanName = newName.trim()
        if (cleanName.isBlank()) {
            onError(IllegalArgumentException("El nombre de la categoría no puede estar vacío."))
            return
        }

        val collection = categoriesCollection(listId)
        collection.get()
            .addOnSuccessListener { categorySnapshot ->
                val duplicate = categorySnapshot.documents.any { document ->
                    document.id != categoryId &&
                        document.getString("name")?.equals(cleanName, ignoreCase = true) == true
                }
                if (duplicate) {
                    onError(IllegalArgumentException("Esta categoría ya existe."))
                    return@addOnSuccessListener
                }

                val categoryReference = collection.document(categoryId)
                categoryReference.update("name", cleanName)
                    .addOnSuccessListener {
                        productsCollection(listId)
                            .whereEqualTo("categoryId", categoryId)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val productDocuments = snapshot.documents

                                fun updateNextBatch(startIndex: Int) {
                                    if (startIndex >= productDocuments.size) {
                                        onSuccess()
                                        return
                                    }

                                    val endIndex = minOf(startIndex + 450, productDocuments.size)
                                    val batch = db.batch()
                                    for (index in startIndex until endIndex) {
                                        batch.update(
                                            productDocuments[index].reference,
                                            "categoryName",
                                            cleanName
                                        )
                                    }
                                    batch.commit()
                                        .addOnSuccessListener { updateNextBatch(endIndex) }
                                        .addOnFailureListener { exception -> onError(exception) }
                                }

                                updateNextBatch(0)
                            }
                            .addOnFailureListener { exception -> onError(exception) }
                    }
                    .addOnFailureListener { exception -> onError(exception) }
            }
            .addOnFailureListener { exception -> onError(exception) }
    }

    /** Elimina la categoría, pero conserva los productos y los deja sin categoría. */
    fun deleteCategory(
        listId: String,
        categoryId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        productsCollection(listId)
            .whereEqualTo("categoryId", categoryId)
            .get()
            .addOnSuccessListener { snapshot ->
                val productDocuments = snapshot.documents

                fun clearNextBatch(startIndex: Int) {
                    if (startIndex >= productDocuments.size) {
                        categoriesCollection(listId).document(categoryId)
                            .delete()
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { exception -> onError(exception) }
                        return
                    }

                    val endIndex = minOf(startIndex + 450, productDocuments.size)
                    val batch = db.batch()
                    for (index in startIndex until endIndex) {
                        batch.update(
                            productDocuments[index].reference,
                            mapOf("categoryId" to "", "categoryName" to "")
                        )
                    }
                    batch.commit()
                        .addOnSuccessListener { clearNextBatch(endIndex) }
                        .addOnFailureListener { exception -> onError(exception) }
                }

                clearNextBatch(0)
            }
            .addOnFailureListener { exception -> onError(exception) }
    }

}
