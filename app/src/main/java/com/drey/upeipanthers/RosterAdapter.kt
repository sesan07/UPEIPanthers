package com.drey.upeipanthers

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
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
        return RosterItemViewHolder(cardView)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RosterItemViewHolder, position: Int) {
        val cardView = holder.cardView
        val rosterItem = rosterItems[position]

        cardView.findViewById<TextView>(R.id.roster_item_name_text_view).text = rosterItem.name

        cardView.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(cardView.context, R.color.colorPrimary))
            val intent = builder.build()
            intent.launchUrl(cardView.context, Uri.parse(rosterItem.link))
        }
    }

    override fun getItemCount(): Int {
        return rosterItems.size
    }

    fun updateRosterItems(rosterItems: List<RosterItem>) {
        this.rosterItems = rosterItems
        notifyDataSetChanged()
    }


    class RosterItemViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)
}