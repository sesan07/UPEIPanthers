package com.drey.upeipanthers

import android.util.Log

private const val TAG = "SportManager"

enum class SportCategory {
    MEN_BASKETBALL,
    MEN_SOCCER,
    MEN_HOCKEY,
    WOMEN_BASKETBALL,
    WOMEN_SOCCER,
    WOMEN_HOCKEY,
    WOMEN_RUGBY,
    TRACK_FIELD,
    SWIMMING,
    CROSS_COUNTRY
}

data class Sport(
    val name: String,
    val category: SportCategory,
    val icon: Int,
    val image: Int,
    val useBoxScoreLink: Boolean,
    val canHaveScore: Boolean,
    val code: String,
    val rosterSeasons: List<String>,
    var fixtureCount: Int = 0
)

class SportManager {
    companion object {
        private val sports = hashMapOf(
            SportCategory.MEN_BASKETBALL to Sport(
                "Men\'s Basketball",
                SportCategory.MEN_BASKETBALL,
                R.drawable.ic_basketball,
                R.drawable.bball_men,
                true,
                true,
                "mbkb",
                listOf("2019-20", "2018-19", "2017-18", "2016-17", "2015-16")
            ),
            SportCategory.MEN_SOCCER to Sport(
                "Men\'s Soccer",
                SportCategory.MEN_SOCCER,
                R.drawable.ic_soccer,
                R.drawable.soccer_men,
                true,
                true,
                "msoc",
                listOf("2019-20", "2018-19", "2017-18", "2016-17", "2015-16")
            ),
            SportCategory.MEN_HOCKEY to Sport(
                "Men\'s Ice Hockey",
                SportCategory.MEN_HOCKEY,
                R.drawable.ic_ice_hockey,
                R.drawable.hockey_men,
                true,
                true,
                "mice",
                listOf("2019-20", "2018-19", "2017-18", "2016-17", "2015-16")
            ),
            SportCategory.WOMEN_BASKETBALL to Sport(
                "Women\'s Basketball",
                SportCategory.WOMEN_BASKETBALL,
                R.drawable.ic_basketball,
                R.drawable.bball_women,
                true,
                true,
                "wbkb",
                listOf("2019-20", "2018-19", "2017-18", "2016-17", "2015-16")
            ),
            SportCategory.WOMEN_SOCCER to Sport(
                "Women\'s Soccer",
                SportCategory.WOMEN_SOCCER,
                R.drawable.ic_soccer,
                R.drawable.soccer_women,
                true,
                true,
                "wsoc",
                listOf("2019-20", "2018-19", "2017-18", "2016-17", "2015-16")
            ),
            SportCategory.WOMEN_HOCKEY to Sport(
                "Women\'s Ice Hockey",
                SportCategory.WOMEN_HOCKEY,
                R.drawable.ic_ice_hockey,
                R.drawable.hockey_women,
                true,
                true,
                "wice",
                listOf("2019-20", "2018-19", "2017-18", "2016-17", "2015-16")
            ),
            SportCategory.WOMEN_RUGBY to Sport(
                "Women\'s Rugby",
                SportCategory.WOMEN_RUGBY,
                R.drawable.ic_rugby,
                R.drawable.rugby_women,
                false,
                true,
                "wrugby",
                listOf("2019-20", "2017-18", "2016-17", "2015-16")
            ),
            SportCategory.TRACK_FIELD to Sport(
                "Track & Field",
                SportCategory.TRACK_FIELD,
                R.drawable.ic_track_field,
                R.drawable.track_field,
                false,
                false,
                "track",
                listOf("2019-20")
            ),
            SportCategory.SWIMMING to Sport(
                "Swimming",
                SportCategory.SWIMMING,
                R.drawable.ic_swimming,
                R.drawable.swimming,
                false,
                false,
                "",
                listOf()
            ),
            SportCategory.CROSS_COUNTRY to Sport(
                "Cross Country",
                SportCategory.CROSS_COUNTRY,
                R.drawable.ic_cross_country,
                R.drawable.cross_country,
                false,
                false,
                "xc",
                listOf("2019-20")
            )
        )

        fun getSport(category: SportCategory): Sport {
            return sports.getOrElse(category, {
                Log.e(TAG, "Unknown sport category: $category")
                sports[SportCategory.MEN_BASKETBALL]!!
            })
        }

        fun getSports(): List<Sport> = sports.values.toList()

        fun getSportCategory(name: String): SportCategory {
            return when (name) {
                "Men\'s Basketball" -> SportCategory.MEN_BASKETBALL
                "Men\'s Soccer" -> SportCategory.MEN_SOCCER
                "Men\'s Ice Hockey" -> SportCategory.MEN_HOCKEY
                "Women\'s Basketball" -> SportCategory.WOMEN_BASKETBALL
                "Women\'s Soccer" -> SportCategory.WOMEN_SOCCER
                "Women\'s Ice Hockey" -> SportCategory.WOMEN_HOCKEY
                "Women\'s Rugby" -> SportCategory.WOMEN_RUGBY
                "Track & Field" -> SportCategory.TRACK_FIELD
                "Track and Field" -> SportCategory.TRACK_FIELD
                "Swimming" -> SportCategory.SWIMMING
                "Cross Country" -> SportCategory.CROSS_COUNTRY
                else -> {
                    Log.e(TAG, "Unknown sport category name: $name")
                    SportCategory.MEN_BASKETBALL
                }
            }
        }
    }
}




