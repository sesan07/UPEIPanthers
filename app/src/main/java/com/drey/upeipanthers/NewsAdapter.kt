package com.drey.upeipanthers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder>(){

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
        val descriptionTextView = cardView.findViewById<TextView>(R.id.description_text_view)

        titleTextView.text = newsItems[position].title
        descriptionTextView.text = newsItems[position].description
        Glide.with(context)
            .load(newsItems[position].image_url)
            .placeholder(R.drawable.ic_insert_picture_icon)
            .into(imageView)
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