package com.drey.upeipanthers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SportsViewModel : ViewModel() {

    private var firstSetup = true
    private var currCategory = MutableLiveData<SportCategory>()

    fun setUp() {
        if (firstSetup) {
            currCategory.value = SportCategory.MEN_BASKETBALL
            firstSetup = false
        }
    }

    fun onCategoryChanged(category: SportCategory) {
        currCategory.value = category
    }

    fun getCurrCategory(): LiveData<SportCategory> {
        return currCategory
    }

}