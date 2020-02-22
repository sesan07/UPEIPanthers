package com.drey.upeipanthers

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

private const val TAG = "HomeFragment"
private const val FIXTURES_FLIP_INTERVAL = 5000
private const val NEWS_FLIP_INTERVAL = 8000

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
        FixtureCategory.TRACK_FIELD to R.drawable.rugby_women,
        FixtureCategory.SWIMMING to R.drawable.rugby_women,
        FixtureCategory.CROSS_COUNTRY to R.drawable.rugby_women
    )

    private lateinit var fixturesViewFlipper: ViewFlipper
    private lateinit var newsViewFlipper: ViewFlipper

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
            populatateImportantFixtures(fixtureItems)
        })

        newsViewFlipper = view.findViewById(R.id.news_view_flipper)
        newsViewModel.getNewsItems().observe(viewLifecycleOwner, Observer<List<NewsItem>>{ newsItems ->
            // update UI
            populatateImportantNews(newsItems)
        })

        return view
    }

    private fun populatateImportantFixtures(fixtureItems: List<FixtureItem>) {
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

            val categoryView = view.findViewById<TextView>(R.id.home_fixture_category_text_view)
            val opponentView = view.findViewById<TextView>(R.id.home_opponent_text_view)
            val descriptionView = view.findViewById<TextView>(R.id.home_fixture_desc_text_view)
            val imageView = view.findViewById<ImageView>(R.id.home_fixture_image_view)

            categoryView.text = item.fixtureCategory.text
            opponentView.text = item.opponent
            descriptionView.text = item.description
            imageView.setImageResource(fixtureCategoryImages[item.fixtureCategory]!!)

            fixturesViewFlipper.addView(view)

        }
        fixturesViewFlipper.flipInterval = FIXTURES_FLIP_INTERVAL
        fixturesViewFlipper.setInAnimation(activity!!, R.anim.slide_in_right)
        fixturesViewFlipper.setOutAnimation(activity!!, R.anim.slide_out_left)
        fixturesViewFlipper.startFlipping()
    }

    private fun populatateImportantNews(newsItems: List<NewsItem>) {
        Log.e(TAG, "populatateImportantNews")

        for (item in newsItems) {
            val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.news_item, null)

            val imageView = view.findViewById<ImageView>(R.id.news_image_view)
            val titleTextView = view.findViewById<TextView>(R.id.title_text_view)
            val descriptionTextView = view.findViewById<TextView>(R.id.description_text_view)

            titleTextView.text = item.title
            descriptionTextView.text = item.description
            GlideApp.with(imageView.context)
                .load(item.image_url)
                .placeholder(R.drawable.ic_insert_picture_icon)
                .into(imageView)

            newsViewFlipper.addView(view)

        }
        newsViewFlipper.flipInterval = NEWS_FLIP_INTERVAL
        newsViewFlipper.setInAnimation(activity!!, R.anim.slide_in_right)
        newsViewFlipper.setOutAnimation(activity!!, R.anim.slide_out_left)
        newsViewFlipper.startFlipping()
    }
}
