
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


    fun renameList(
        listId: String,
        newName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("list")
            .document(listId)
            .update("name", newName.trim())
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getListName(
        listId: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return db.collection("list")
            .document(listId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val name = snapshot.getString("name") ?: "Mi lista"
                    onSuccess(name)
                }
            }
    }

    fun getSharedUserEmails(
        listId: String,
        onSuccess: (List<String>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("list")
            .document(listId)
            .get()
            .addOnSuccessListener { document ->

                val members = document.get("member") as? List<String>
                    ?: emptyList()

                val ownerId = document.getString("ownerId")

                val sharedUserIds = members.filter { it != ownerId }

                if (sharedUserIds.isEmpty()) {
                    onSuccess(emptyList())
                    return@addOnSuccessListener
                }

                val emails = mutableListOf<String>()
                var completed = 0

                sharedUserIds.forEach { userId ->
                    db.collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { userDocument ->

                            userDocument.getString("email")?.let {
                                emails.add(it)
                            }

                            completed++

                            if (completed == sharedUserIds.size) {
                                onSuccess(emails)
                            }
                        }
                        .addOnFailureListener { exception ->
                            onError(exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    // Eliminar una lista y sus subcolecciones de productos y categorías.
    // Firestore no elimina automáticamente las subcolecciones al borrar el documento padre.
    fun deleteList(
        listId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val listReference = db.collection("list").document(listId)

        listReference.collection("products").get()
            .addOnSuccessListener { productsSnapshot ->
                listReference.collection("categories").get()
                    .addOnSuccessListener { categoriesSnapshot ->
                        val references = productsSnapshot.documents.map { it.reference } +
                            categoriesSnapshot.documents.map { it.reference }

                        fun deleteNextBatch(startIndex: Int) {
                            if (startIndex >= references.size) {
                                listReference.delete()
                                    .addOnSuccessListener { onSuccess() }
                                    .addOnFailureListener { exception -> onError(exception) }
                                return
                            }

                            val endIndex = minOf(startIndex + 450, references.size)
                            val batch = db.batch()
                            for (index in startIndex until endIndex) {
                                batch.delete(references[index])
                            }
                            batch.commit()
                                .addOnSuccessListener { deleteNextBatch(endIndex) }
                                .addOnFailureListener { exception -> onError(exception) }
                        }

                        deleteNextBatch(0)
                    }
                    .addOnFailureListener { exception -> onError(exception) }
            }
            .addOnFailureListener { exception -> onError(exception) }
    }

}
