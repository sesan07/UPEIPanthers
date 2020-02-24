package com.drey.upeipanthers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

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
            try {
                newsItems.value = Repository.getNewsItems()
            }
            catch (e: Exception) {
                Log.e(TAG, "Exception while loading new Items: $e")
            }
        }
    }
}
