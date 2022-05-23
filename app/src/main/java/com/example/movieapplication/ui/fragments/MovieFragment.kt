package com.example.movieapplication.ui.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.BlurTransformation
import coil.transform.GrayscaleTransformation
import com.complete.newsreporter.utils.Resources
import com.example.movieapplication.R
import com.example.movieapplication.adapters.MovieAdapter
import com.example.movieapplication.database.MovieDatabase
import com.example.movieapplication.database.MovieRepository
import com.example.movieapplication.models.Result
import com.example.movieapplication.models.images.Poster
import com.example.movieapplication.utils.Constants
import com.example.movieapplication.utils.Constants.Companion.IMAGERV
import com.example.movieapplication.utils.Constants.Companion.SIMILARRV
import com.example.movieapplication.utils.Constants.Companion.TAG
import com.example.movieapplication.utils.Constants.Companion.URLIMAGE
import com.example.movieapplication.viewmodels.MovieViewModel
import com.example.movieapplication.viewmodels.MovieViewModelFactory
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_popular_movie.*
import java.text.DecimalFormat


class MovieFragment : Fragment() {

    private lateinit var imageAdapter: MovieAdapter
    private lateinit var movieAdapter: MovieAdapter
    private val args: MovieFragmentArgs by navArgs()
    private var ls: List<Result>? = null
    private var lsi: List<Poster>? = null
    private lateinit var viewModel: MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Constants.isOnline(requireActivity())){
            setViews(args)
        }else{
            Toast.makeText(activity,"You don't have internet connection to proceed",Toast.LENGTH_SHORT).show()
            nsv.visibility = View.GONE
        }


    }

    @SuppressLint("SetTextI18n")
    private fun setViews(args: MovieFragmentArgs) {
        val movie = args.movie
        iv_movie_poster.load(URLIMAGE + movie.poster_path) {
            placeholder(R.drawable.icons8_movie)
        }
        iv_bg_movie.load(URLIMAGE + movie.poster_path) {
            crossfade(500)
            transformations(
                GrayscaleTransformation(),
                BlurTransformation(requireContext(), 10f)

            )
        }
        viewModel(movie.id!!)

    }

    @SuppressLint("SetTextI18n")
    private fun viewModel(id: Int) {
        val repository = MovieRepository(MovieDatabase(requireActivity()))
        val viewModelFactory = MovieViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieViewModel::class.java)
        viewModel.getDetails(id)
        viewModel.movieDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { movieResponse ->
                        Log.d(TAG, movieResponse.toString())
                        val df = DecimalFormat("0.0")
                        tv_movie_title.text = movieResponse.title
                        tv_release_date.text = "Realase Date - " + movieResponse.release_date
                        tv_movie_desc.text = "Overview - " + movieResponse.overview
                        tv_movie_vote.text =
                            "Rating - " + df.format(movieResponse.vote_average).toString()
                        tv_runtime.text = "Runtime - " + movieResponse.runtime + " min"
                        var s = ""
                        for (i in movieResponse.genres!!.indices) {
                            s += movieResponse.genres[i].name
                            if (i != movieResponse.genres.size - 1) {
                                s += ", "
                            }
                        }
                        tv_genre.text = "Genre - " + s
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), "An Error occured $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resources.Loading -> {
                }
            }
        }
        viewModel.getSimilar(id)
        viewModel.similarMovieLs.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { movieResponse ->
                        Log.d(TAG, movieResponse.results.toString())
                        ls = movieResponse.results
                        setupRecyclerView()
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), "An Error occured $it", Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, "Something is wrong")
                    }
                }
                is Resources.Loading -> {
                }
            }
        })
        viewModel.getImages(id)
        viewModel.imagesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { imageResponse ->
                        Log.d(TAG, imageResponse.toString())
                        Log.d(TAG, imageResponse.posters.toString())
                        lsi = imageResponse.posters
                        setUpRecyclerView()
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), "An Error occured $it", Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, "Something is wrong")
                    }
                }
                is Resources.Loading -> {
                }
            }
        })

    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(ls, lsi, type = SIMILARRV)
        rv_similarm.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapter
        }
        movieAdapter.setOnItemCLickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            this.findNavController().navigate(R.id.movieFragment, bundle)
        }

    }

    private fun setUpRecyclerView() {
        imageAdapter = MovieAdapter(ls, lsi, type = IMAGERV)
        rv_movies_images.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = imageAdapter
        }
    }
}