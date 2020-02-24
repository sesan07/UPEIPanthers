package com.drey.upeipanthers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TAG = "NewsViewModel"
class NewsViewModel : ViewModel() {

    private val newsItems: MutableLiveData<List<NewsItem>> = MutableLiveData(listOf())
    private var attemptedLoad = false

    fun setUp() {
        if (!attemptedLoad) {
            loadNewsItems()
            attemptedLoad = true
        }
    }

    fun getNewsItems(): LiveData<List<NewsItem>> {
        return newsItems
    }

    private fun loadNewsItems() {
        // Do an asynchronous operation to fetch newsItems.
        viewModelScope.launch {
            newsItems.value = Repository.getNewsItems()
        }
    }
}
