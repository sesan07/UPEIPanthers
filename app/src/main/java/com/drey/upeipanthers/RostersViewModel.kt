package com.drey.upeipanthers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "RostersViewModel"

class RostersViewModel : ViewModel()  {

    private var firstSetup = true
    var isLoading = false
        private set

    var currCategory = SportCategory.MEN_BASKETBALL
        private set
    var currSeasonIndex = 0
        private set

    private var isNetworkAvailable = false

    // Map of sport categories to roster names
    private var categoryRosterItems = hashMapOf<SportCategory, MutableList<List<RosterItem>>>()
    // Roster items for current category
    private var currRosterItems = MutableLiveData(listOf<RosterItem>())

    fun setUp() {
        if (firstSetup) {
            for (category in SportCategory.values()) {
                val sport = SportManager.getSport(category)
                val categorySeasons = mutableListOf<List<RosterItem>>()  // List of seasons in a sport category
                sport.rosterSeasons.forEach {
                    categorySeasons.add(listOf())  // List of roster items in a season
                }

                categoryRosterItems[category] = categorySeasons
            }

            firstSetup = false
        }
    }

    private fun loadRosterItems() {
        if (isLoading) {
            Log.d(TAG, "Roster is already loading")
            return
        }

        if (!isNetworkAvailable) {
            Log.e(TAG, "Network is not available, can't load rosters")
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val sport = SportManager.getSport(currCategory)
                val rosterSeasons = sport.rosterSeasons
                val season = rosterSeasons.getOrElse(currSeasonIndex) {
                    Log.d(TAG, "${sport.name} has no season at index $currSeasonIndex")
                    ""
                }

                if (sport.code.isNotEmpty() && season.isNotEmpty()) {
                    var rosterItems = Repository.getRosterItems(sport.code, season)
                    rosterItems = rosterItems.sortedWith(compareBy { it.name })
                    categoryRosterItems[sport.category]!![currSeasonIndex] = rosterItems
                }

            } catch (e: Exception) {
                Log.e(TAG, "Exception while loading roster items: $e")
            }

            isLoading = false
            onSeasonChanged(currSeasonIndex)
        }
    }

    fun getCurrRosterItems(): LiveData<List<RosterItem>> {
        return currRosterItems
    }

    private fun optionsChanged(category: SportCategory, seasonIndex: Int) {
        currSeasonIndex = seasonIndex
        currCategory = category

        val categorySeasons = categoryRosterItems.getOrElse(currCategory, { listOf<List<RosterItem>>() })
        var isValidSeason = true
        val rosterItems = categorySeasons.getOrElse(currSeasonIndex) {
            isValidSeason = false
            listOf()
        }
        if (isValidSeason && rosterItems.isEmpty()) {
            loadRosterItems()
        }

        currRosterItems.value = rosterItems
    }

    fun onCategoryChanged(category: SportCategory) {
        optionsChanged(category, 0)
    }

    fun onSeasonChanged(seasonIndex: Int) {
        optionsChanged(currCategory, seasonIndex)
    }

    fun onNetworkAvailabilityChanged(isNetworkNowAvailable: Boolean) {
        isNetworkAvailable = isNetworkNowAvailable

        if (currRosterItems.value.isNullOrEmpty() && isNetworkAvailable) {
            Log.d(TAG, "Network is now available, loading rosters")
            loadRosterItems()
        }
    }
}