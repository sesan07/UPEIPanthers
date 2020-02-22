package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "FixturesFragment"

class FixturesFragment : Fragment() {

    private val model: FixturesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fixtures, container, false)

        val expandableListView = view.findViewById<ExpandableListView>(R.id.category_expandable_list_view)

        val fixtureCategoriesAdapter = FixtureCategoriesAdapter(
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

        val layoutManager = LinearLayoutManager(view.context)
        val fixturesAdapter = FixturesAdapter(findNavController())

        model.getCurrFixtureItems().observe(viewLifecycleOwner, Observer<List<FixtureItem>>{ fixtureItems ->
            // update UI
            fixturesAdapter.updateFixtureItems(fixtureItems)
            fixtureCategoriesAdapter.updateCategoryCounts(model.getCategoryCounts())
        })

        val recyclerView = view.findViewById(R.id.fixtures_recycler_view) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fixturesAdapter

        // Inflate the layout for this fragment
        return view
    }
}
