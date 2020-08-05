package com.drey.upeipanthers

import android.graphics.Typeface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "RosterAdapter"

class RosterAdapter : RecyclerView.Adapter<RosterAdapter.RosterItemViewHolder>() {

    private var rosterItems = listOf<RosterItem>()

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RosterItemViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.roster_item, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return RosterItemViewHolder(cardView)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RosterItemViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val cardView = holder.cardView
        val rosterItem = rosterItems[position]

        cardView.findViewById<TextView>(R.id.roster_item_name_text_view).text = rosterItem.name
//        cardView.findViewById<TextView>(R.id.month_text_view).text = rosterItem.month
//        cardView.findViewById<TextView>(R.id.day_text_view).text = rosterItem.day
//
//        val homeTeamTextView = cardView.findViewById<TextView>(R.id.home_team_text_view)
//        val awayTeamTextView = cardView.findViewById<TextView>(R.id.away_team_text_view)
//        homeTeamTextView.text = rosterItem.homeTeam
//        awayTeamTextView.text = rosterItem.awayTeam
//        if (rosterItem.isHomeGame) {
//            homeTeamTextView.typeface = boldTypeFace
//            awayTeamTextView.typeface = normalTypeFace
//        }
//        else {
//            homeTeamTextView.typeface = normalTypeFace
//            awayTeamTextView.typeface = boldTypeFace
//        }
//
//        when {
//            rosterItem.canHaveScore and rosterItem.hasScore -> {
//                cardView.findViewById<LinearLayout>(R.id.score_linear_layout).visibility = View.VISIBLE
//                cardView.findViewById<LinearLayout>(R.id.scoreless_linear_layout).visibility = View.GONE
//
//                cardView.findViewById<TextView>(R.id.comments_text_view).text = rosterItem.comment
//
//                val homeScoreTextView = cardView.findViewById<TextView>(R.id.home_score_text_view)
//                val awayScoreTextView = cardView.findViewById<TextView>(R.id.away_score_text_view)
//                homeScoreTextView.text = rosterItem.homeScore
//                awayScoreTextView.text = rosterItem.awayScore
//                if (rosterItem.isHomeGame) {
//                    homeScoreTextView.typeface = boldTypeFace
//                    awayScoreTextView.typeface = normalTypeFace
//                }
//                else {
//                    homeScoreTextView.typeface = normalTypeFace
//                    awayScoreTextView.typeface = boldTypeFace
//                }
//            }
//
//            rosterItem.canHaveScore and !rosterItem.hasScore -> {
//                cardView.findViewById<LinearLayout>(R.id.scoreless_linear_layout).visibility = View.VISIBLE
//                cardView.findViewById<LinearLayout>(R.id.score_linear_layout).visibility = View.GONE
//
//                cardView.findViewById<TextView>(R.id.time_text_view).text = rosterItem.time
//            }
//
//            else -> {
//                cardView.findViewById<LinearLayout>(R.id.scoreless_linear_layout).visibility = View.GONE
//                cardView.findViewById<LinearLayout>(R.id.score_linear_layout).visibility = View.GONE
//            }
//        }

        cardView.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(cardView.context, R.color.colorPrimary))
            val intent = builder.build()
            intent.launchUrl(cardView.context, Uri.parse(rosterItem.link))
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return rosterItems.size
    }

    fun updateRosterItems(rosterItems: List<RosterItem>) {
        this.rosterItems = rosterItems
        notifyDataSetChanged()
    }


    class RosterItemViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)
}