package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG = "SportsFragment"

class SportsFragment : Fragment() {

    private val sportsViewModel: SportsViewModel by activityViewModels()
    private val fixturesViewModel: FixturesViewModel by activityViewModels()
    private val rostersViewModel: RostersViewModel by activityViewModels()

    private lateinit var categoriesAdapter: SportCategoriesAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: SportsViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sports, container, false)

        val categoriesListView = view.findViewById<ExpandableListView>(R.id.category_expandable_list_view)

        val typeFace = ResourcesCompat.getFont(requireContext(), R.font.raleway_semi_bold)
        categoriesAdapter = SportCategoriesAdapter(
            requireContext(),
            sportsViewModel.getCurrCategory().value!!,
            SportCategory.values().toList(),
            typeFace!!
        )

        categoriesListView.setAdapter(categoriesAdapter)

        categoriesListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val selectedCategory = SportCategory.values()[childPosition]
            sportsViewModel.onCategoryChanged(selectedCategory)

            categoriesAdapter.setCurrCategory(selectedCategory)
            categoriesListView.collapseGroup(groupPosition)
            false
        }

        sportsViewModel.getCurrCategory().observe(viewLifecycleOwner, Observer { category ->
            fixturesViewModel.onCategoryChanged(category)
            rostersViewModel.onCategoryChanged(category)
        })

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
                1 -> tab.text = "Rosters"
            }
        }.attach()
    }
}