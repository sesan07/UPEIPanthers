package com.drey.upeipanthers

import android.util.Log
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.annotation.*
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.NoSuchElementException

private const val TAG = "Repository"
private const val BASE_URL = "http://gopanthersgo.ca"
private const val NEWS_URL = "/landing/headlines-featured?feed=rss_2.0"
private const val FIXTURES_URL = "/composite?print=rss"

private const val DATE_FORMAT_STR = "EEE, dd MMM yyyy HH:mm:ss z"

class Repository {

    @Xml
    class NewsModel {
        @Path("channel")
        @Element(name = "item")
        lateinit var items: List<NewsItemModel>
    }

    @Xml
    class NewsItemModel {
        @PropertyElement
        lateinit var title: String

        @PropertyElement
        lateinit var link: String

        @PropertyElement
        lateinit var description: String

        @Path("enclosure")
        @Attribute(name = "url")
        lateinit var url: String
    }

    @Xml
    class FixturesModel {
        @Path("channel")
        @Element(name = "item")
        lateinit var items: List<FixtureItemModel>
    }

    @Xml
    class FixtureItemModel {
        @PropertyElement
        lateinit var title: String

        @PropertyElement
        lateinit var link: String

        @PropertyElement
        lateinit var description: String

        @PropertyElement
        lateinit var category: String

        @PropertyElement
        lateinit var pubDate: String
    }

    interface WebService {
        @GET(NEWS_URL)
        suspend fun getNews(): NewsModel

        @GET(FIXTURES_URL)
        suspend fun getFixtures(): FixturesModel
    }

    companion object {

        suspend fun getNewsItems(): List<NewsItem> {
            val newsItems = mutableListOf<NewsItem>()

            val webService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TikXmlConverterFactory.create(
                    TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .build().create(WebService::class.java)

            val items = webService.getNews().items

            for (item in items) {
                newsItems.add(NewsItem(
                    item.title,
                    item.link,
                    item.description,
                    item.url.substringBefore("?")
                ))
            }

            return newsItems
        }

        suspend fun getFixtureItems(): List<FixtureItem> {
            val fixtureItems = mutableListOf<FixtureItem>()

            val webService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TikXmlConverterFactory.create(
                    TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .build().create(WebService::class.java)

            val items = webService.getFixtures().items

            for (item in items) {
                try {
                    fixtureItems.add(
                        FixtureItem(
                            item.title,
                            item.link,
                            item.description,
                            when (item.category) {
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
                                else -> throw NoSuchElementException("${item.category} category")
                            },
                            SimpleDateFormat(DATE_FORMAT_STR, Locale.ENGLISH).parse(item.pubDate)!!
                        )
                    )
                } catch (ex: NoSuchElementException) {
                    Log.e(TAG, ex.toString())
                } catch (ex: Exception) {
                    Log.e(TAG, ex.toString())
                }

            }

            return fixtureItems
        }
    }
}