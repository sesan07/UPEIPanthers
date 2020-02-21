package com.drey.upeipanthers

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView


class FixtureCategoriesAdapter(
    private val context: Context,
    private val categoriesGroup: String,
    private val categoryItems: List<String>
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return categoriesGroup
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
        groupTextView.text = categoriesGroup
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
        val expandedListTextView = view!!.findViewById<TextView>(R.id.fixture_category_text_view)
        expandedListTextView.text = categoryItem
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return 1
    }
}