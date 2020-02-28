package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "FixturesFragment"

class FixturesFragment : Fragment() {

    private val model: FixturesViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTextView: TextView
    private lateinit var fixtureCategoriesAdapter: FixtureCategoriesAdapter
    private lateinit var fixturesAdapter: FixturesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fixtures, container, false)

        val expandableListView = view.findViewById<ExpandableListView>(R.id.category_expandable_list_view)

        fixtureCategoriesAdapter = FixtureCategoriesAdapter(
            view.context, model.currCategory,
            FixtureCategory.values().toList(),
            model.getCategoryCounts()
        )

        expandableListView.setAdapter(fixtureCategoriesAdapter)

        expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val selectedCategory = FixtureCategory.values()[childPosition]

            fixtureCategoriesAdapter.setCurrCategory(selectedCategory)
            model.categoryChanged(selectedCategory)
            expandableListView.collapseGroup(groupPosition)
            false
        }

        progressBar = view.findViewById(R.id.fixtures_progress_bar) as ProgressBar
        emptyTextView = view.findViewById(R.id.fixtures_empty_text_view) as TextView
        emptyTextView.visibility = View.GONE

        val boldTypeFace = ResourcesCompat.getFont(view.context, R.font.raleway_extra_bold)
        val normalTypeFace = ResourcesCompat.getFont(view.context, R.font.raleway_semi_bold)

        val layoutManager = LinearLayoutManager(view.context)
        fixturesAdapter = FixturesAdapter(boldTypeFace!!, normalTypeFace!!)
        recyclerView = view.findViewById(R.id.fixtures_recycler_view) as RecyclerView

        model.getCurrFixtureItems().observe(viewLifecycleOwner, Observer<List<FixtureItem>>{ fixtureItems ->
            updateFixturesUI(fixtureItems)
        })

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fixturesAdapter

        return view
    }

    private fun updateFixturesUI(fixtureItems: List<FixtureItem>) {
        if (!model.loaded)
            return

        progressBar.visibility = View.GONE

        if (fixtureItems.isEmpty())
            emptyTextView.visibility = View.VISIBLE
        else
            emptyTextView.visibility = View.GONE

        fixturesAdapter.updateFixtureItems(fixtureItems)
        recyclerView.smoothScrollToPosition(0)
        fixtureCategoriesAdapter.updateCategoryCounts(model.getCategoryCounts())
    }
}
