package com.drey.upeipanthers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "RosterFragment"

class RosterFragment : Fragment() {

    private val model: RostersViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTextView: TextView
    private lateinit var rosterAdapter: RosterAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var seasonsSpinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_roster, container, false)

        progressBar = view.findViewById(R.id.roster_progress_bar) as ProgressBar
        emptyTextView = view.findViewById(R.id.roster_empty_text_view) as TextView
        emptyTextView.visibility = View.GONE

        seasonsSpinner = view.findViewById(R.id.roster_season_spinner)
        spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_text_view)
        seasonsSpinner.adapter = spinnerAdapter
        seasonsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onRosterSeasonSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        recyclerView = view.findViewById(R.id.roster_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        rosterAdapter = RosterAdapter()
        recyclerView.adapter = rosterAdapter

        model.getCurrRosterItems().observe(viewLifecycleOwner, Observer{ rosterItems ->
            updateRosterUI(rosterItems)
        })

        return view
    }

    private fun updateRosterUI(rosterItems: List<RosterItem>) {
        if (model.isLoading) {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
            return
        } else {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }


        val currCategory = model.currCategory
        val categorySeasons = SportManager.getSport(currCategory).rosterSeasons

        spinnerAdapter.clear()
        if (categorySeasons.isNotEmpty()) {
            seasonsSpinner.visibility = View.VISIBLE
            spinnerAdapter.addAll(categorySeasons)
            seasonsSpinner.setSelection(model.currSeasonIndex)
        } else {
            seasonsSpinner.visibility = View.INVISIBLE
        }

        emptyTextView.visibility = if (rosterItems.isEmpty()) View.VISIBLE else View.GONE

        rosterAdapter.updateRosterItems(rosterItems)
        recyclerView.smoothScrollToPosition(0)
    }

    private fun onRosterSeasonSelected(index: Int) {
        model.onSeasonChanged(index)
    }
}