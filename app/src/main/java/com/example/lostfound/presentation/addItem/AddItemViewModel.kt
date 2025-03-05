package com.example.lostfound.presentation.addItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostfound.presentation.repository.FirestoreRepository
import com.example.lostfound.data.model.LostFoundItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel() {

    private val _addItemState = MutableStateFlow<Result<Unit>?>(null)
    val addItemState = _addItemState.asStateFlow()


    //function for adding items
    fun addItem(item: LostFoundItem){
        viewModelScope.launch {
            firestoreRepository.addItem(item).collectLatest { result ->
                if(result.isSuccess){
                    _addItemState.value = Result.success(Unit)
                }else {
                    _addItemState.value = Result.failure(result.exceptionOrNull()!!)
                }
            }
        }
    }

}