package com.example.myapplication.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.FoodItem

class FoodItemViewModel : ViewModel() {

    private val foodItemDataMap: MutableMap<Long, FoodItem> = mutableMapOf()
    private val foodItem: MutableLiveData<FoodItem> = MutableLiveData()
    fun setFoodItem(foodId: Long, foodItem: FoodItem) {
        foodItemDataMap[foodId] = foodItem
    }

    fun getFoodItem(foodId: Long): FoodItem? {
        return foodItemDataMap[foodId]
    }
}
