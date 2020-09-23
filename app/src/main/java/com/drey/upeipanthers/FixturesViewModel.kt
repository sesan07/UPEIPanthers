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

    private var firstSetup = true
    var isLoading = false
        private set

    private var isNetworkAvailable = false

    private var currCategory = SportCategory.MEN_BASKETBALL

    // Map of sport categories to Fixture items
    private var categoryFixtureItems = hashMapOf<SportCategory, List<FixtureItem>>()
    // Fixture items for all categories
    private var allFixtureItems = MutableLiveData(listOf<FixtureItem>())
    // Fixture items for current category
    private var currFixtureItems = MutableLiveData(listOf<FixtureItem>())

    fun setUp() {
        if (firstSetup) {
            for (category in SportCategory.values()) {
                categoryFixtureItems[category] = listOf()
            }

            firstSetup = false
        }
    }

    fun getCurrFixtureItems(): LiveData<List<FixtureItem>> {
        return currFixtureItems
    }

    fun getAllFixtureItems(): LiveData<List<FixtureItem>> {
        return allFixtureItems
    }

    private fun loadFixtureItems() {
        if (isLoading) {
            Log.d(TAG, "Fixtures is already loading")
            return
        }

        if (!isNetworkAvailable) {
            Log.e(TAG, "Network is not available, can't load fixtures")
            return
        }
        // Do an asynchronous operation to fetch fixtureItems.
        viewModelScope.launch {
            isLoading = true

            var allItems = listOf<FixtureItem>()
            try {
                allItems = Repository.getFixtureItems()

                for (category in SportCategory.values()) {
                    val categoryItems = allItems.filter {
                        it.sportCategory == category
                    }
                    categoryFixtureItems[category] = categoryItems
                    SportManager.getSport(category).fixtureCount = categoryItems.size
                }
            }
            catch (e: Exception) {
                Log.e(TAG, "Exception while loading fixture Items: $e")
            }

            isLoading = false
            allFixtureItems.value = allItems
            onCategoryChanged(currCategory)
        }
    }

    fun onCategoryChanged(category: SportCategory) {
        currCategory = category
        currFixtureItems.value = categoryFixtureItems.getOrElse(currCategory, { listOf() })
    }

    fun onNetworkAvailabilityChanged(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable

        if (currFixtureItems.value.isNullOrEmpty() && isNetworkAvailable) {
            Log.d(TAG, "Network is now available, loading fixtures")
            loadFixtureItems()
        }
    }
}