package com.drey.upeipanthers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class FixturesAdapter() : RecyclerView.Adapter<FixturesAdapter.FixtureItemViewHolder>(){

    private var fixtureItems = listOf<FixtureItem>()

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureItemViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fixture_item, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return FixtureItemViewHolder(cardView)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: FixtureItemViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val cardView = holder.cardView
        val fixtureItem = fixtureItems[position]

        cardView.findViewById<TextView>(R.id.month_text_view).text = fixtureItem.month
        cardView.findViewById<TextView>(R.id.day_text_view).text = fixtureItem.day
        cardView.findViewById<TextView>(R.id.time_text_view).text = fixtureItem.time
        cardView.findViewById<TextView>(R.id.home_team_text_view).text = fixtureItem.homeTeam
        cardView.findViewById<TextView>(R.id.away_team_text_view).text = fixtureItem.awayTeam

        if (fixtureItems[position].hasScore) {
            cardView.findViewById<LinearLayout>(R.id.score_linear_layout).visibility = View.VISIBLE
            cardView.findViewById<Space>(R.id.scoreless_space).visibility = View.GONE

            cardView.findViewById<TextView>(R.id.home_score_text_view).text = fixtureItem.homeScore
            cardView.findViewById<TextView>(R.id.away_score_text_view).text = fixtureItem.awayScore
            cardView.findViewById<TextView>(R.id.comments_text_view).text = fixtureItem.comment
        }
        else {
            cardView.findViewById<Space>(R.id.scoreless_space).visibility = View.VISIBLE
            cardView.findViewById<LinearLayout>(R.id.score_linear_layout).visibility = View.GONE
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return fixtureItems.size
    }

    fun updateFixtureItems(fixtureItems: List<FixtureItem>) {
        this.fixtureItems = fixtureItems
        notifyDataSetChanged()
    }


    class FixtureItemViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)
}