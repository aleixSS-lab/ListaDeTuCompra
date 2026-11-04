package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class TagRepository {

    private val db = FirebaseFirestore.getInstance()


    // Recoger la lista por default de los tags

    fun getDefaultTags(
        onSuccess: (List<String>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return db.collection("default_tags")
            .addSnapshotListener { snapshot, error -> // Escuchar cambios en la colección "default_tags" (snapshot guarda la información de la colección)
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val tags = snapshot.documents.mapNotNull { document ->
                        document.getString("name")
                    }.sorted()

                    onSuccess(tags)
                }
            }
    }



    fun createTag(
        name: String,
        ownerId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val tag = hashMapOf(
            "name" to name,
            "ownerId" to ownerId,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("tags")
            .add(tag)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getTags(
        ownerId: String,
        onSuccess: (List<String>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return db.collection("tags")
            .whereEqualTo("ownerId", ownerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val tags = snapshot.documents.mapNotNull { document ->
                        document.getString("name")
                    }

                    onSuccess(tags)
                }
            }
    }

    fun deleteTag(
        name: String,
        ownerId: String,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        db.collection("tags")
            .whereEqualTo("ownerId", ownerId)
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    doc.reference.delete()
                }
                onSuccess()
            }
            .addOnFailureListener { onError(it) }
    }
}
