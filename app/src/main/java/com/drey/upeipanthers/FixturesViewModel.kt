package com.drey.upeipanthers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TAG = "FixturesViewModel"
class FixturesViewModel : ViewModel() {

    private var attemptedLoad = false
    var currCategory = FixtureCategory.values()[0]
        private set

    private var categoryFixtureItems = hashMapOf<FixtureCategory, List<FixtureItem>>()
    private val currFixtureItems = MutableLiveData<List<FixtureItem>>(listOf())

    fun setUp() {
        if (!attemptedLoad) {
            for (category in FixtureCategory.values()) {
                categoryFixtureItems[category] = listOf()
            }

            loadFixtureItems()
            attemptedLoad = true
        }
    }


    fun getFixtureItems(): LiveData<List<FixtureItem>> {
        return currFixtureItems
    }

    private fun loadFixtureItems() {
        // Do an asynchronous operation to fetch newsItems.
        viewModelScope.launch {
            val allFixtureItems = Repository.getFixtureItems()

            for (category in FixtureCategory.values()) {
                categoryFixtureItems[category] = allFixtureItems.filter {
                    it.fixtureCategory == category
                }
            }

            categoryChanged(currCategory)
        }
    }

    fun categoryChanged(category: FixtureCategory) {
        currCategory = category
        currFixtureItems.value = categoryFixtureItems[category]
    }

    fun getCategorySizes(): List<Int> {
        val sizes = mutableListOf<Int>()
        val fixtures = FixtureCategory.values()
        for (fixture in fixtures) {
            sizes.add(categoryFixtureItems[fixture]!!.size)
        }

        return sizes
    }
}