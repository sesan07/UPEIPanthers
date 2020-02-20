package com.drey.upeipanthers

import java.util.*

enum class FixtureCategory() {
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

data class FixtureItem(val title: String,
                  val link: String,
                  val description: String,
                  val category: FixtureCategory,
                  val date: Date)