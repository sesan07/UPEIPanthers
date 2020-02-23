package com.drey.upeipanthers

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

private const val TAG = "FixtureCatAdapter"

class FixtureCategoriesAdapter(
    private val context: Context,
    private var currCategory: FixtureCategory,
    private val categoryItems: List<FixtureCategory>,
    private var categoryCounts: List<Int>
) : BaseExpandableListAdapter() {

    private val fixtureCategoryIcons = hashMapOf (
        FixtureCategory.MEN_BASKETBALL to R.drawable.ic_basketball,
        FixtureCategory.MEN_SOCCER to R.drawable.ic_soccer,
        FixtureCategory.MEN_HOCKEY to R.drawable.ic_ice_hockey,
        FixtureCategory.WOMEN_BASKETBALL to R.drawable.ic_basketball,
        FixtureCategory.WOMEN_SOCCER to R.drawable.ic_soccer,
        FixtureCategory.WOMEN_HOCKEY to R.drawable.ic_ice_hockey,
        FixtureCategory.WOMEN_RUGBY to R.drawable.ic_rugby,
        FixtureCategory.TRACK_FIELD to R.drawable.ic_track_field,
        FixtureCategory.SWIMMING to R.drawable.ic_swimming,
        FixtureCategory.CROSS_COUNTRY to R.drawable.ic_cross_country
    )

    override fun getGroup(groupPosition: Int): Any {
        return currCategory
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.sport_category_chooser, null)
        }
        val groupTextView = view!!.findViewById<TextView>(R.id.fixture_category_chooser_text_view)
        groupTextView.setTypeface(null, Typeface.BOLD)
        groupTextView.text = currCategory.text
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return categoryItems.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return categoryItems[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return 0
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val categoryItem = categoryItems[childPosition]

        var view = convertView
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.sport_category, null)
        }
        val categoryTextView = view!!.findViewById<TextView>(R.id.fixture_category_text_view)
        val categoryCountTextView = view.findViewById<TextView>(R.id.category_count_view)
        val icon = view.findViewById<ImageView>(R.id.category_image_view)

        categoryTextView.text = categoryItem.text
        categoryCountTextView.text = categoryCounts[childPosition].toString()
        icon.setImageResource(fixtureCategoryIcons[categoryItem]!!)

        if (categoryItem == currCategory)
            view.findViewById<FrameLayout>(R.id.highlighted_bg).visibility = View.VISIBLE
        else
            view.findViewById<FrameLayout>(R.id.highlighted_bg).visibility = View.INVISIBLE

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return 1
    }

    fun setCurrCategory(category: FixtureCategory) {
        currCategory = category
        notifyDataSetChanged()
    }

    fun updateCategoryCounts(categoryCounts: List<Int>) {
        this.categoryCounts = categoryCounts
        notifyDataSetChanged()
    }
}