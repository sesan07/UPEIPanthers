package com.drey.upeipanthers

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "NewsAdapter"

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder>(){

    private var newsItems = listOf<NewsItem>()

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return NewsItemViewHolder(cardView)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val cardView = holder.cardView
        val imageView = cardView.findViewById<ImageView>(R.id.news_image_view)
        val titleTextView = cardView.findViewById<TextView>(R.id.title_text_view)

        val newsItem = newsItems[position]

        titleTextView.text = newsItem.title
        GlideApp.with(imageView.context)
            .load(newsItem.image_url)
            .placeholder(R.drawable.temp_image)
            .dontTransform()
            .into(imageView)

        cardView.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(cardView.context, R.color.colorPrimary))
            val intent = builder.build()
            intent.launchUrl(cardView.context, Uri.parse(newsItem.link))
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return newsItems.size
    }

    fun updateNewsItems(newsItems: List<NewsItem>) {
        this.newsItems = newsItems
        notifyDataSetChanged()
    }

    class NewsItemViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)
}
