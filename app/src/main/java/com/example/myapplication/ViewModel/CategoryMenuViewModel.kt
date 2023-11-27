package com.example.myapplication.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.FoodItem

class CategoryMenuViewModel : ViewModel() {

    private val categoryDataMap: MutableMap<String, List<FoodItem>> = mutableMapOf()
    private val categoryItems: MutableLiveData<List<FoodItem>> = MutableLiveData()
    fun setCategoryItems(categoryName: String, items: List<FoodItem>) {
        categoryDataMap[categoryName] = items
        categoryItems.value = items
    }
    fun getCategoryItems(categoryName: String): List<FoodItem>? {
        return categoryDataMap[categoryName]
    }
}
