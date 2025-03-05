package com.example.lostfound.presentation.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostfound.data.model.ItemStatus
import com.example.lostfound.data.model.LostFoundItem
import com.example.lostfound.presentation.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LostViewModel @Inject constructor(private val repository: FirestoreRepository): ViewModel() {

    private val _addItemState = MutableStateFlow<Result<Boolean>?>(null)
    val addItemState: StateFlow<Result<Boolean>?> get() = _addItemState

    val lostItems = repository.getItem(ItemStatus.LOST)
    val foundItems = repository.getItem(ItemStatus.FOUND)

    //add item logic
    fun addItem(item: LostFoundItem) {
        viewModelScope.launch {
            repository.addItem(item).collect{ result ->
                result.onSuccess {
                    _addItemState.value = Result.success(true)
                }.onFailure { error ->
                    _addItemState.value = Result.failure(error)
                }
            }
        }
    }
}