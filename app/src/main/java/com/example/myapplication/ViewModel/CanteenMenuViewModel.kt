
package com.example.myapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.FoodItem

class CanteenMenuViewModel : ViewModel() {
    private val _canteenItems = MutableLiveData<List<FoodItem>>()

    val canteenItems: LiveData<List<FoodItem>>
        get() = _canteenItems

    fun setCanteenItems(items: List<FoodItem>) {
        _canteenItems.value = items
    }
}
