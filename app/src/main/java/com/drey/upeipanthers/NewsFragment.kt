package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NewsFragment : Fragment() {

    private val model: NewsViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTextView: TextView
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        progressBar = view.findViewById(R.id.news_progress_bar) as ProgressBar
        emptyTextView = view.findViewById(R.id.news_empty_text_view) as TextView
        emptyTextView.visibility = View.GONE

        val layoutManager = LinearLayoutManager(view.context)
        newsAdapter = NewsAdapter()

        model.getNewsItems().observe(viewLifecycleOwner, Observer<List<NewsItem>>{ newsItems ->
            updateNewsUI(newsItems)
        })

        val recyclerView = view.findViewById(R.id.news_recycler_view) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = newsAdapter

        return view
    }

    private fun updateNewsUI(newsItems: List<NewsItem>) {
        if (!model.loaded)
            return

        progressBar.visibility = View.GONE

        if (newsItems.isEmpty())
            emptyTextView.visibility = View.VISIBLE
        else
            emptyTextView.visibility = View.GONE

        newsAdapter.updateNewsItems(newsItems)
    }
}
