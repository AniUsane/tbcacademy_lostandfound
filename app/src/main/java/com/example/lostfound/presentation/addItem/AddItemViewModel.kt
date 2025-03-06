package com.example.lostfound.presentation.addItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostfound.data.model.ItemStatus
import com.example.lostfound.data.model.LostFoundItem
import com.example.lostfound.presentation.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel() {

    private val _addItemState = MutableStateFlow<Result<Unit>?>(null)
    val addItemState = _addItemState.asStateFlow()



    //function for adding items
    fun addItem(title: String, description: String, location: String, status: String) {
        val newItem = LostFoundItem(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            location = location,
            image = "",
            date = Date(),
            userId = "user123",
            status = if (status == "LOST") ItemStatus.LOST else ItemStatus.FOUND
        )

        viewModelScope.launch {
            firestoreRepository.addItem(newItem).collectLatest { result ->
                _addItemState.value = result
            }
        }
    }






}