package com.drey.upeipanthers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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

        val categoryEnumValues = FixtureCategory.values()
        val categoryList = List(categoryEnumValues.size) {
            categoryEnumValues[it].text
        }

        val fixtureCategoriesAdapter = FixtureCategoriesAdapter(view.context, "Categories", categoryList)
        expandableListView.setAdapter(fixtureCategoriesAdapter)
        expandableListView.setOnGroupExpandListener {

        }
        expandableListView.setOnChildClickListener { parent, view, groupPosition, childPosition, id ->

            false
        }


        val layoutManager = LinearLayoutManager(view.context)
        val fixturesAdapter = FixturesAdapter()

        model.getFixtureItems().observe(viewLifecycleOwner, Observer<List<FixtureItem>>{ fixtureItems ->
            // update UI
            fixturesAdapter.updateFixtureItems(fixtureItems)
        })

        val recyclerView = view.findViewById(R.id.fixtures_recycler_view) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fixturesAdapter

        // Inflate the layout for this fragment
        return view
    }
}
