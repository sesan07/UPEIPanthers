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
    var isLoading = false
        private set

    private var isNetworkAvailable = false

    private val currNewsItems: MutableLiveData<List<NewsItem>> = MutableLiveData(listOf())

    fun getNewsItems(): LiveData<List<NewsItem>> {
        return currNewsItems
    }

    private fun loadNewsItems() {
        if (isLoading) {
            Log.d(TAG, "News is already loading")
            return
        }

        if (!isNetworkAvailable) {
            Log.e(TAG, "Network is not available, can't load news")
            return
        }

        // Do an asynchronous operation to fetch newsItems.
        viewModelScope.launch {
            isLoading = true

            var items = listOf<NewsItem>()
            try {
                items = Repository.getNewsItems()
            }
            catch (e: Exception) {
                Log.e(TAG, "Exception while loading new Items: $e")
            }

            isLoading = false
            currNewsItems.value = items
        }
    }

    fun onNetworkAvailabilityChanged(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable

        if (currNewsItems.value.isNullOrEmpty() && isNetworkAvailable) {
            Log.d(TAG, "Network is now available, loading news")
            loadNewsItems()
        }
    }
}
