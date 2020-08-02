package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SportsFragment : Fragment() {

    private val model: FixturesViewModel by viewModels()

    private lateinit var categoriesAdapter: SportCategoriesAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: SportsViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sports, container, false)

        val categoriesListView = view.findViewById<ExpandableListView>(R.id.category_expandable_list_view)

        categoriesAdapter = SportCategoriesAdapter(
            view.context, model.currCategory,
            SportCategory.values().toList()
        )

        categoriesListView.setAdapter(categoriesAdapter)

        categoriesListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val selectedCategory = SportCategory.values()[childPosition]

            categoriesAdapter.setCurrCategory(selectedCategory)
            model.categoryChanged(selectedCategory)
            categoriesListView.collapseGroup(groupPosition)
            false
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPagerAdapter = SportsViewPagerAdapter(this)
        viewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = viewPagerAdapter


        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Fixtures"
                1 -> tab.text = "Roster"
            }
        }.attach()
    }
}