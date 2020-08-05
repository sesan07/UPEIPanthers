package com.drey.upeipanthers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val sportsViewModel: SportsViewModel by viewModels()
    private val fixturesViewModel: FixturesViewModel by viewModels()
    private val rostersViewModel: RostersViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sportsViewModel.setUp()
        fixturesViewModel.setUp()
        rostersViewModel.setUp()
//        newsViewModel.setUp()

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        setupBottomNavMenu(navController)

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d(TAG, "Network Available")
                onNetworkAvailabilityChanged(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)

                Log.e(TAG, "Network Lost")
                onNetworkAvailabilityChanged(false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.e(TAG, "Network Unavailable")
                onNetworkAvailabilityChanged(false)
            }
        })
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    private fun onNetworkAvailabilityChanged(isAvailable: Boolean) {
        fixturesViewModel.onNetworkAvailabilityChanged(isAvailable)
        newsViewModel.onNetworkAvailabilityChanged(isAvailable)
        rostersViewModel.onNetworkAvailabilityChanged(isAvailable)
    }
}

@GlideModule
class AppNameGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.apply { RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(
            ObjectKey(System.currentTimeMillis().toShort())
        ) }
    }

}
