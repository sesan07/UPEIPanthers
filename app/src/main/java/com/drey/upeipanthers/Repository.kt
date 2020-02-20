package com.drey.upeipanthers

import android.util.Log
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.annotation.*
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET

private const val TAG = "Repository"
private const val baseUrl = "http://gopanthersgo.ca"
private const val newsUrl = "/landing/headlines-featured?feed=rss_2.0"

class Repository {

    @Xml
    class NewsModel {
        @Path("channel")
        @Element(name = "item")
        lateinit var items: List<ItemModel>
    }

    @Xml(name = "item")
    class ItemModel {
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

    interface WebService {
        @GET(newsUrl)
        suspend fun getNews(): NewsModel
    }

    companion object {

        suspend fun getNewsItems(): List<NewsItem> {
            val newsItems = mutableListOf<NewsItem>()

            val webService = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(TikXmlConverterFactory.create(
                    TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .build().create(WebService::class.java)

            val items = webService.getNews().items

            for (item in items) {
//                Log.e(TAG, item.title)
//                Log.e(TAG, item.url.substringBefore("?"))
                newsItems.add(NewsItem(
                    item.title,
                    item.link,
                    item.description,
                    item.url.substringBefore("?")
                ))
            }
            return newsItems
        }
    }
}