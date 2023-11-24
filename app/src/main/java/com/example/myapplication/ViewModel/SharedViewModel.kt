package com.example.myapplication.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.CanteenItem
class SharedViewModel : ViewModel() {
    // Use MutableLiveData or MutableStateFlow, depending on your implementation
    private val _canteenItems = MutableLiveData<List<CanteenItem>>()

    val canteenItems: LiveData<List<CanteenItem>>
        get() = _canteenItems
    fun setCanteenItems(items: List<CanteenItem>) {
        _canteenItems.value = items
    }
}
