package com.drey.upeipanthers

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private const val TAG = "FixtureItem"
private const val TEAM_NAME = "UPEI"

enum class FixtureCategory(val text: String) {
    MEN_BASKETBALL("Men\'s Basketball"),
    MEN_SOCCER("Men\'s Soccer"),
    MEN_HOCKEY("Men\'s Ice Hockey"),
    WOMEN_BASKETBALL("Women\'s Basketball"),
    WOMEN_SOCCER("Women\'s Soccer"),
    WOMEN_HOCKEY("Women\'s Ice Hockey"),
    WOMEN_RUGBY("Women\'s Rugby"),
    TRACK_FIELD("Track & Field"),
    SWIMMING("Swimming"),
    CROSS_COUNTRY("Cross Country")
}

private val VICTORY_COMMENTS = listOf(
    "EZ WIN",
    "Too easy",
    "Huzzah!!",
    "For Glory!!",
    "For Honor!!",
    ":^)",
    ":D"
)

private const val LOSS_COMMENT = "Meh.."

class FixtureItem(
    title: String,
    link: String,
    val description: String,
    categoryStr: String,
    dateStr: String,
    score: String,
    opponent1: String
) {

    var fixtureCategory: FixtureCategory
    var homeTeam = ""
    var awayTeam = ""
    var homeScore = ""
    var awayScore = ""
    var year = ""
    var month = ""
    var day = ""
    var time = ""
    var comment = ""
    var isHomeGame = false
    var isVictory = false
    var hasScore = false
    var isImportant = false
    var opponent = ""

    init {

        fixtureCategory = when (categoryStr) {
            "Men\'s Basketball" -> FixtureCategory.MEN_BASKETBALL
            "Men\'s Soccer" -> FixtureCategory.MEN_SOCCER
            "Men\'s Ice Hockey" -> FixtureCategory.MEN_HOCKEY
            "Women\'s Basketball" -> FixtureCategory.WOMEN_BASKETBALL
            "Women\'s Soccer" -> FixtureCategory.WOMEN_SOCCER
            "Women\'s Ice Hockey" -> FixtureCategory.WOMEN_HOCKEY
            "Women\'s Rugby" -> FixtureCategory.WOMEN_RUGBY
            "Track and Field" -> FixtureCategory.TRACK_FIELD
            "Swimming" -> FixtureCategory.SWIMMING
            "Cross Country" -> FixtureCategory.CROSS_COUNTRY
            else -> throw NoSuchElementException(categoryStr)
        }

        val isAwayGame = opponent1.startsWith("at ")
        val isExhibition = opponent1.startsWith("vs. ")
        isHomeGame = !isAwayGame

        when {
            isAwayGame -> {
                homeTeam = opponent1.substringAfter("at ")
                awayTeam = TEAM_NAME
                opponent = homeTeam
            }
            isExhibition -> {
                homeTeam = TEAM_NAME
                awayTeam = opponent1.substringAfter("vs. ")
                opponent = awayTeam
            }
            else -> {
                homeTeam = TEAM_NAME
                awayTeam = opponent1
                opponent = opponent1
            }
        }

        if (score.isNotEmpty()) {
            hasScore = true

            val arr = score.split(", ")
            val arr2 = arr[1].split("-")

            if (arr[0] == "W") {
                isVictory = true
                comment = VICTORY_COMMENTS[Random.nextInt(VICTORY_COMMENTS.size)]

                if (isAwayGame) {
                    homeScore = min(arr2[0].toInt(), arr2[1].toInt()).toString()
                    awayScore = max(arr2[0].toInt(), arr2[1].toInt()).toString()
                }
                else {
                    homeScore = max(arr2[0].toInt(), arr2[1].toInt()).toString()
                    awayScore = min(arr2[0].toInt(), arr2[1].toInt()).toString()
                }
            }
            else {
                comment = LOSS_COMMENT

                if (isAwayGame) {
                    homeScore = max(arr2[0].toInt(), arr2[1].toInt()).toString()
                    awayScore = min(arr2[0].toInt(), arr2[1].toInt()).toString()
                }
                else {
                    homeScore = min(arr2[0].toInt(), arr2[1].toInt()).toString()
                    awayScore = max(arr2[0].toInt(), arr2[1].toInt()).toString()
                }
            }
        }

        val today = Date()
        val date: Date = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            .parse(dateStr)!!

        val diff = date.time - today.time
        val daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        isImportant = daysDiff >= 0

        year = SimpleDateFormat("yyyy", Locale.ENGLISH).format(date)
        month = SimpleDateFormat("MMM", Locale.ENGLISH).format(date)
        day = SimpleDateFormat("dd", Locale.ENGLISH).format(date)
        time = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date)
    }

}