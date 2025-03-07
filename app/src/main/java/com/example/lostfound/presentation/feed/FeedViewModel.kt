package com.example.lostfound.presentation.feed

import androidx.lifecycle.ViewModel
import com.example.lostfound.data.model.LostFoundItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FeedViewModel: ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _allLostItems = MutableStateFlow<List<LostFoundItem>>(emptyList())
    private val _allFoundItems = MutableStateFlow<List<LostFoundItem>>(emptyList())

    private val _filteredLostItems = MutableStateFlow<List<LostFoundItem>>(emptyList())
    private val _filteredFoundItems = MutableStateFlow<List<LostFoundItem>>(emptyList())

    val filteredLostItems: StateFlow<List<LostFoundItem>> = _filteredLostItems
    val filteredFoundItems: StateFlow<List<LostFoundItem>> = _filteredFoundItems

    fun setLostItems(items: List<LostFoundItem>) {
        _allLostItems.value = items
        filterLostItems()
    }

    fun setFoundItems(items: List<LostFoundItem>) {
        _allFoundItems.value = items
        filterFoundItems()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filterLostItems()
        filterFoundItems()
    }

    private fun filterLostItems() {
        val query = _searchQuery.value
        _filteredLostItems.value = if (query.isEmpty()) {
            _allLostItems.value
        } else {
            _allLostItems.value.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

    private fun filterFoundItems() {
        val query = _searchQuery.value
        _filteredFoundItems.value = if (query.isEmpty()) {
            _allFoundItems.value
        } else {
            _allFoundItems.value.filter { it.title.contains(query, ignoreCase = true) }
        }
    }
}