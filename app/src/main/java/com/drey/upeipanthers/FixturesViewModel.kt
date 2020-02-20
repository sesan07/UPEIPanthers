package com.drey.upeipanthers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TAG = "FixturesViewModel"
class FixturesViewModel : ViewModel() {

    private val fixtureItems: MutableLiveData<List<FixtureItem>> = MutableLiveData(listOf())
    private var attemptedLoad = false

    fun setUp() {
        if (!attemptedLoad) {
            loadFixtureItems()
            attemptedLoad = true
        }
    }


    fun getFixtureItems(): LiveData<List<FixtureItem>> {
        return fixtureItems
    }

    private fun loadFixtureItems() {
        // Do an asynchronous operation to fetch newsItems.
        viewModelScope.launch {
            fixtureItems.value = Repository.getFixtureItems()
        }
    }
}