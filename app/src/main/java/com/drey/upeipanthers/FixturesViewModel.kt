package com.drey.upeipanthers

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
    private val allFixtureItems = MutableLiveData<List<FixtureItem>>(listOf())
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


    fun getCurrFixtureItems(): LiveData<List<FixtureItem>> {
        return currFixtureItems
    }

    fun getAllFixtureItems(): LiveData<List<FixtureItem>> {
        return allFixtureItems
    }

    private fun loadFixtureItems() {
        // Do an asynchronous operation to fetch newsItems.
        viewModelScope.launch {
            val allItems = Repository.getFixtureItems()
            allFixtureItems.value = allItems

            for (category in FixtureCategory.values()) {
                categoryFixtureItems[category] = allItems.filter {
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

    fun getCategoryCounts(): List<Int> {
        val counts = mutableListOf<Int>()
        val fixtures = FixtureCategory.values()
        for (fixture in fixtures) {
            counts.add(categoryFixtureItems[fixture]!!.size)
        }

        return counts
    }
}