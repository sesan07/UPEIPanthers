package com.drey.upeipanthers

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
private const val TICKET_LINK = "https://script.google.com/a/macros/upei.ca/s/AKfycbzN2Ke1ZG5ttp8ij9vhNwLT87yYH3LNwR9d6LhLuarZmLWYMiJx/exec"

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
            populateImportantFixtures(fixtureItems)
        })

        newsViewFlipper = view.findViewById(R.id.news_view_flipper)
        newsViewModel.getNewsItems().observe(viewLifecycleOwner, Observer<List<NewsItem>>{ newsItems ->
            // update UI
            populateImportantNews(newsItems)
        })

        view.findViewById<Button>(R.id.tickets_button).setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            val intent = builder.build()
            intent.launchUrl(activity!!, Uri.parse(TICKET_LINK))
        }


        return view
    }

    private fun populateImportantFixtures(fixtureItems: List<FixtureItem>) {
        Log.e(TAG, "populatateImportantFixtures")

        val importantFixtureItems = mutableListOf<FixtureItem>()
        for (item in fixtureItems) {
            if (item.isImportant)
                importantFixtureItems.add(item)
        }
        if (importantFixtureItems.size == 0)
            return

        for (item in importantFixtureItems) {
            val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.home_fixture_item, null)

            val dateContextView = view.findViewById<TextView>(R.id.home_date_context_text_view)
            val dateView = view.findViewById<TextView>(R.id.home_date_text_view)
            val categoryView = view.findViewById<TextView>(R.id.home_fixture_category_text_view)
            val opponentView = view.findViewById<TextView>(R.id.home_opponent_text_view)
//            val descriptionView = view.findViewById<TextView>(R.id.home_fixture_desc_text_view)
            val imageView = view.findViewById<ImageView>(R.id.home_fixture_image_view)

            dateContextView.text = item.dateContext.name
            dateView.text = item.date
            categoryView.text = item.fixtureCategory.text
            opponentView.text = item.opponent
//            descriptionView.text = item.description
            imageView.setImageResource(fixtureCategoryImages[item.fixtureCategory]!!)

            fixturesViewFlipper.addView(view)

        }
        fixturesViewFlipper.flipInterval = FIXTURES_FLIP_INTERVAL
        fixturesViewFlipper.setInAnimation(activity!!, R.anim.slide_in_right)
        fixturesViewFlipper.setOutAnimation(activity!!, R.anim.slide_out_left)
        fixturesViewFlipper.startFlipping()
    }

    private fun populateImportantNews(newsItems: List<NewsItem>) {



        var i = 0
        while (i < newsItems.size && i < MAX_NEWS_ITEMS) {
            val item = newsItems[i]

            val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
        newsViewFlipper.flipInterval = NEWS_FLIP_INTERVAL
        newsViewFlipper.setInAnimation(activity!!, R.anim.slide_in_right)
        newsViewFlipper.setOutAnimation(activity!!, R.anim.slide_out_left)
        newsViewFlipper.startFlipping()
    }
}
