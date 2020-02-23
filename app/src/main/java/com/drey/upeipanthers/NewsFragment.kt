package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NewsFragment : Fragment() {

    private val model: NewsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        val layoutManager = LinearLayoutManager(view.context)
        val newsAdapter = NewsAdapter()

        model.getNewsItems().observe(viewLifecycleOwner, Observer<List<NewsItem>>{ newsItems ->
            // update UI
            newsAdapter.updateNewsItems(newsItems)
        })

        val recyclerView = view.findViewById(R.id.news_recycler_view) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = newsAdapter

        // Inflate the layout for this fragment
        return view
    }
}
