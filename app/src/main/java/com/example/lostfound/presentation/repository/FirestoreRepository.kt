package com.example.lostfound.presentation.repository

import com.example.lostfound.data.model.ItemStatus
import com.example.lostfound.data.model.LostFoundItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor() {
    private val dataBase: FirebaseFirestore = FirebaseFirestore.getInstance()

    //adds new item to firestore
    fun addItem(item: LostFoundItem): Flow<Result<Boolean>> = flow {
        try {
            val docRef = dataBase.collection("items").document()
            val newItem = item.copy(id = docRef.id)
            docRef.set(newItem).await()

            emit(Result.success(true))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    //gets items from the firestore based on their status
    fun getItem(status: ItemStatus): Flow<List<LostFoundItem>> = callbackFlow {
        val listener = dataBase.collection("items")
            .whereEqualTo("status", status.name)
            .addSnapshotListener { snapshot, _ ->
                if(snapshot != null) {
                    val items = snapshot.toObjects(LostFoundItem::class.java)
                    trySend(items)
                }
            }
        awaitClose{listener.remove()}
    }
}