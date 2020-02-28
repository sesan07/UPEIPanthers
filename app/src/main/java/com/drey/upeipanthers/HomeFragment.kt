package com.drey.upeipanthers

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

private const val TAG = "HomeFragment"
private const val FIXTURES_FLIP_INTERVAL = 5000
private const val NEWS_FLIP_INTERVAL = 8000
private const val MAX_NEWS_ITEMS = 5
private const val TICKET_LINK = "https://script.google.com/a/macros/upei.ca/s/AKfycbzddQmFMkviRcu_7ktWOQVvXwu8qPaXJyhmdqKZPCDA3y6o4tw/exec"

class HomeFragment : Fragment() {

    private val fixturesViewModel: FixturesViewModel by activityViewModels()
    private val newsViewModel: NewsViewModel by activityViewModels()

    private val fixtureCategoryImages = hashMapOf (
        FixtureCategory.MEN_BASKETBALL to R.drawable.bball_men,
        FixtureCategory.MEN_SOCCER to R.drawable.soccer_men,
        FixtureCategory.MEN_HOCKEY to R.drawable.hockey_men,
        FixtureCategory.WOMEN_BASKETBALL to R.drawable.bball_women,
        FixtureCategory.WOMEN_SOCCER to R.drawable.soccer_women,
        FixtureCategory.WOMEN_HOCKEY to R.drawable.hockey_women,
        FixtureCategory.WOMEN_RUGBY to R.drawable.rugby_women,
        FixtureCategory.TRACK_FIELD to R.drawable.track_field,
        FixtureCategory.SWIMMING to R.drawable.swimming,
        FixtureCategory.CROSS_COUNTRY to R.drawable.cross_country
    )

    private lateinit var fixturesViewFlipper: HomeViewFlipper
    private lateinit var newsViewFlipper: HomeViewFlipper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel.setUp()
        fixturesViewModel.setUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        fixturesViewFlipper = view.findViewById(R.id.fixtures_view_flipper)
        fixturesViewModel.getAllFixtureItems().observe(viewLifecycleOwner, Observer<List<FixtureItem>>{ fixtureItems ->
            updateImportantFixtures(fixtureItems)
        })

        newsViewFlipper = view.findViewById(R.id.news_view_flipper)
        newsViewModel.getNewsItems().observe(viewLifecycleOwner, Observer<List<NewsItem>>{ newsItems ->
            // update UI
            updateImportantNews(newsItems)
        })

        view.findViewById<Button>(R.id.tickets_button).setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            val intent = builder.build()
            intent.launchUrl(activity!!, Uri.parse(TICKET_LINK))
        }

        return view
    }

    private fun updateImportantFixtures(fixtureItems: List<FixtureItem>) {
        fixturesViewFlipper.removeAllViews()

        val importantFixtureItems = mutableListOf<FixtureItem>()
        for (item in fixtureItems) {
            if (item.isUpcoming)
                importantFixtureItems.add(item)
        }

        if (!fixturesViewModel.loaded) {
            val view = layoutInflater.inflate(R.layout.home_loading_view, null)
            fixturesViewFlipper.addView(view)
            fixturesViewFlipper.stopFlipping()
            return
        }

        if (importantFixtureItems.isEmpty()) {
            val view = layoutInflater.inflate(R.layout.home_fixture_item, null)

            val dateView = view.findViewById<TextView>(R.id.home_date_text_view)
            val categoryView = view.findViewById<TextView>(R.id.home_fixture_category_text_view)
            val opponentView = view.findViewById<TextView>(R.id.home_opponent_text_view)

            dateView.visibility = View.GONE
            categoryView.visibility = View.GONE

            opponentView.text = resources.getString(R.string.home_no_fixtures)

            fixturesViewFlipper.addView(view)
            fixturesViewFlipper.stopFlipping()
            return
        }

        for (item in importantFixtureItems) {
            val view = layoutInflater.inflate(R.layout.home_fixture_item, null)

            val dateView = view.findViewById<TextView>(R.id.home_date_text_view)
            val categoryView = view.findViewById<TextView>(R.id.home_fixture_category_text_view)
            val opponentView = view.findViewById<TextView>(R.id.home_opponent_text_view)
            val imageView = view.findViewById<ImageView>(R.id.home_fixture_image_view)

            dateView.text = item.date
            categoryView.text = item.fixtureCategory.text
            opponentView.text = item.opponent
            imageView.setImageResource(fixtureCategoryImages[item.fixtureCategory]!!)

            fixturesViewFlipper.addView(view)

        }

        if (importantFixtureItems.size <= 1) {
            fixturesViewFlipper.stopFlipping()
            return
        }

        fixturesViewFlipper.flipInterval = FIXTURES_FLIP_INTERVAL
        fixturesViewFlipper.setInAnimation(activity!!, R.anim.slide_in_right)
        fixturesViewFlipper.setOutAnimation(activity!!, R.anim.slide_out_left)
        fixturesViewFlipper.startFlipping()
    }

    private fun updateImportantNews(newsItems: List<NewsItem>) {
        newsViewFlipper.removeAllViews()

        if (!newsViewModel.loaded) {
            val view = layoutInflater.inflate(R.layout.home_loading_view, null)
            newsViewFlipper.addView(view)
            newsViewFlipper.stopFlipping()
            return
        }

        if (newsItems.isEmpty()) {
            val cardView = layoutInflater.inflate(R.layout.news_item, null) as CardView

            val imageView = cardView.findViewById<ImageView>(R.id.news_image_view)
            val titleTextView = cardView.findViewById<TextView>(R.id.title_text_view)
            val detailsTextView = cardView.findViewById<TextView>(R.id.news_details_text_view)

            detailsTextView.visibility = View.GONE

            titleTextView.text = resources.getString(R.string.home_no_news)
            imageView.alpha = 0.1f

            newsViewFlipper.addView(cardView)
            newsViewFlipper.stopFlipping()
            return
        }

        var i = 0
        while (i < newsItems.size && i < MAX_NEWS_ITEMS) {
            val item = newsItems[i]

            val cardView = layoutInflater.inflate(R.layout.news_item, null) as CardView

            val imageView = cardView.findViewById<ImageView>(R.id.news_image_view)
            val titleTextView = cardView.findViewById<TextView>(R.id.title_text_view)

            titleTextView.text = item.title
            GlideApp.with(imageView.context)
                .load(item.image_url)
                .placeholder(R.drawable.temp_image)
                .dontTransform()
                .into(imageView)

            cardView.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
                val intent = builder.build()
                intent.launchUrl(activity!!, Uri.parse(item.link))
            }

            newsViewFlipper.addView(cardView)

            i++
        }

        if (newsItems.size <= 1) {
            newsViewFlipper.stopFlipping()
            return
        }

        newsViewFlipper.flipInterval = NEWS_FLIP_INTERVAL
        newsViewFlipper.setInAnimation(activity!!, R.anim.slide_in_right)
        newsViewFlipper.setOutAnimation(activity!!, R.anim.slide_out_left)
        newsViewFlipper.startFlipping()
    }
}
