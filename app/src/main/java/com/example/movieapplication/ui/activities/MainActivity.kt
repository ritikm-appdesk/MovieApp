package com.example.movieapplication.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movieapplication.R
import com.example.movieapplication.databinding.ActivityMainBinding
import com.example.movieapplication.utils.Constants.Companion.isOnline
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding  get() = _binding

    private lateinit var navController: NavController
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[1].isEnabled = false
        if(!isOnline(this)){
            bottomNavigationView.visibility = View.GONE
            bottomAppBar.visibility = View.GONE
            fab.visibility = View.GONE
        }else{
            fragment.findNavController().navigate(R.id.popularMovieFragment)
            bottomNavigationView.setupWithNavController(fragment.findNavController())
            fab.setOnClickListener {
                for (i in 0 until fragment.childFragmentManager.backStackEntryCount) {
                    fragment.findNavController().popBackStack()
                }
                fragment.findNavController().navigate(R.id.searchFragment)

            }
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.popularMovieFragment-> {
                        fragment.findNavController().navigate(R.id.popularMovieFragment)
                    }
                    R.id.topRatedMovieFragment-> {
                        fragment.findNavController().navigate(R.id.topRatedMovieFragment)
                    }
                }
                for (i in 0 until fragment.childFragmentManager.backStackEntryCount) {
                    fragment.findNavController().popBackStack()
                }
                true

            }
        }
    }

}