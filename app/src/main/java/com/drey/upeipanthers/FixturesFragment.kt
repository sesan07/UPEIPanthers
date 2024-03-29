package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var fixturesAdapter: FixturesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fixtures, container, false)

        progressBar = view.findViewById(R.id.fixtures_progress_bar) as ProgressBar
        emptyTextView = view.findViewById(R.id.fixtures_empty_text_view) as TextView
        emptyTextView.visibility = View.GONE

        val boldTypeFace = ResourcesCompat.getFont(requireContext(), R.font.raleway_extra_bold)
        val normalTypeFace = ResourcesCompat.getFont(requireContext(), R.font.raleway_semi_bold)

        val layoutManager = LinearLayoutManager(requireContext())
        fixturesAdapter = FixturesAdapter(boldTypeFace!!, normalTypeFace!!)
        recyclerView = view.findViewById(R.id.fixtures_recycler_view) as RecyclerView

        model.getCurrFixtureItems().observe(viewLifecycleOwner, Observer{ fixtureItems ->
            updateFixturesUI(fixtureItems)
        })

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fixturesAdapter

        return view
    }

    private fun updateFixturesUI(fixtureItems: List<FixtureItem>) {
        if (model.isLoading)
            return

        progressBar.visibility = View.GONE

        if (fixtureItems.isEmpty())
            emptyTextView.visibility = View.VISIBLE
        else
            emptyTextView.visibility = View.GONE

        fixturesAdapter.updateFixtureItems(fixtureItems)

        // Scroll to closest fixture (first fixture that's not upcoming)
        var i = 0
        var closestIndex = 0
        while (i < fixtureItems.size){
            if (!fixtureItems[i].isUpcoming) {
                closestIndex = i;
                break
            }
            i++
        }
        recyclerView.smoothScrollToPosition(closestIndex)
    }
}
