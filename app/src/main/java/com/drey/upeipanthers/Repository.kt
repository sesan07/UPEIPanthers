package com.drey.upeipanthers

import android.util.Log
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.annotation.*
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url
import kotlin.NoSuchElementException

private const val TAG = "Repository"
private const val BASE_URL = "http://gopanthersgo.ca"
private const val NEWS_URL = "/landing/headlines-featured?feed=rss_2.0"
private const val FIXTURES_URL = "/composite?print=rss"
private const val MAX_FIXTURES = 400

private fun getRosterUrl(sportCode: String, season: String): String {
    return "/sports/$sportCode/$season/roster?feed=rss_2.0"
}


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
        var url: String? = null
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

        @PropertyElement(name = "ps:score")
        lateinit var score: String

        @PropertyElement(name = "ps:opponent")
        lateinit var opponent: String
    }

    @Xml
    class RosterModel {
        @Path("channel")
        @Element(name = "item")
        lateinit var items: List<RosterItemModel>

        @Path("channel")
        @Element(name = "image")
        var image: RosterImageModel? = null
    }

    @Xml
    class RosterImageModel {
        @PropertyElement
        lateinit var title: String

        @PropertyElement
        lateinit var url: String
    }

    @Xml
    class RosterItemModel {
        @PropertyElement
        lateinit var title: String

        @PropertyElement
        lateinit var link: String
    }

    interface WebService {
        @GET(NEWS_URL)
        suspend fun getNews(): NewsModel

        @GET(FIXTURES_URL)
        suspend fun getFixtures(): FixturesModel

        @GET
        suspend fun getRoster(@Url url: String): RosterModel
    }

    companion object {
        private val webService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(TikXmlConverterFactory.create(
                TikXml.Builder().exceptionOnUnreadXml(false).build()))
            .build().create(WebService::class.java)

        suspend fun getNewsItems(): List<NewsItem> {
            val items = webService.getNews().items
            val newsItems = mutableListOf<NewsItem>()

            for (item in items) {
                if (item.url === null) {
                    continue
                }
                newsItems.add(NewsItem(
                    item.title,
                    item.link,
                    item.description,
                    item.url!!.substringBefore("?").replace("http://", "https://")
                ))
            }

            return newsItems
        }

        suspend fun getFixtureItems(): List<FixtureItem> {
            val items = webService.getFixtures().items
            val fixtureItems = mutableListOf<FixtureItem>()

            var i = items.size - 1
            var count = 0
            while (count < MAX_FIXTURES && i >= 0) {
                val item = items[i]
                try {
                    fixtureItems.add(
                        FixtureItem(
                            item.title,
                            item.link,
                            item.description,
                            item.category,
                            item.pubDate,
                            item.score,
                            item.opponent
                        )
                    )
                } catch (ex: NoSuchElementException) {
                    Log.e(TAG, "Unknown category: ${ex.message}")
                } catch (ex: UninitializedPropertyAccessException) {
                    Log.e(TAG, "Expected property for FixtureItem is missing: ${ex.message}")
                } catch (ex: Exception) {
                    Log.e(TAG, ex.toString())
                }

                count++
                i--
            }

            return fixtureItems
        }

        suspend fun getRosterItems(sportCode: String, season: String): List<RosterItem> {
            val rosterModel = webService.getRoster(getRosterUrl(sportCode, season))
            val rosterItems = mutableListOf<RosterItem>()

            val items = rosterModel.items
            for (item in items) {
                rosterItems.add(
                    RosterItem(
                        item.title,
                        item.link
                    )
                )
            }

            return rosterItems
        }
    }
}