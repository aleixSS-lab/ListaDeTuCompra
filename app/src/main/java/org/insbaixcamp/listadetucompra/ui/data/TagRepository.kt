package org.insbaixcamp.listadetucompra.ui.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class TagRepository {

    private val db = FirebaseFirestore.getInstance()

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
}
