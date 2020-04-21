package com.drey.upeipanthers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "FixturesViewModel"
class FixturesViewModel : ViewModel() {

    private var attemptedLoad = false
    var loaded = false
        private set
    var currCategory = FixtureCategory.values()[0]
        private set

    // Map of categories to Fixture items
    private var categoryFixtureItems = hashMapOf<FixtureCategory, List<FixtureItem>>()
    // Fixture items for all categories
    private val allFixtureItems = MutableLiveData<List<FixtureItem>>(listOf())
    // Fixture items for current category
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
        // Do an asynchronous operation to fetch fixtureItems.
        viewModelScope.launch {
            try {
                val allItems = Repository.getFixtureItems()
                loaded = true
                allFixtureItems.value = allItems

                for (category in FixtureCategory.values()) {
                    categoryFixtureItems[category] = allItems.filter {
                        it.fixtureCategory == category
                    }
                }

                categoryChanged(currCategory)
            }
            catch (e: Exception) {
                Log.e(TAG, "Exception while loading fixture Items: $e")
            }
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