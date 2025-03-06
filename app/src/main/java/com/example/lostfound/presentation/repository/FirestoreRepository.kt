package com.example.lostfound.presentation.repository

import android.net.Uri
import com.example.lostfound.data.model.ItemStatus
import com.example.lostfound.data.model.LostFoundItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val database: FirebaseFirestore
) {
    fun addItem(item: LostFoundItem): Flow<Result<Unit>> = callbackFlow {
        database.collection("lostFoundItems")
            .document(item.id)
            .set(item)
            .addOnSuccessListener { trySend(Result.success(Unit)) }
            .addOnFailureListener { trySend(Result.failure(it)) }
        awaitClose {}
    }

    fun getItems(status: ItemStatus): Flow<List<LostFoundItem>> = callbackFlow {
        val listener = database.collection("lostFoundItems")
            .whereEqualTo("status", status.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val items = it.toObjects(LostFoundItem::class.java)
                    trySend(items)
                }
            }
        awaitClose { listener.remove() }
    }
}