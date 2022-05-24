package com.example.movieapplication.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.BlurTransformation
import coil.transform.GrayscaleTransformation
import com.example.movieapplication.R
import com.example.movieapplication.adapters.MovieAdapter
import com.example.movieapplication.database.MovieRepository
import com.example.movieapplication.models.Result
import com.example.movieapplication.models.images.Poster
import com.example.movieapplication.utils.Constants
import com.example.movieapplication.utils.Constants.Companion.IMAGERECYCLERVIEW
import com.example.movieapplication.utils.Constants.Companion.SIMILARRECYCLERVIEW
import com.example.movieapplication.utils.Constants.Companion.URLFORIMAGE
import com.example.movieapplication.utils.Resources
import com.example.movieapplication.viewmodels.MovieViewModel
import com.example.movieapplication.viewmodels.MovieViewModelFactory
import kotlinx.android.synthetic.main.fragment_movie.*
import java.text.DecimalFormat


class MovieFragment : Fragment() {

    private lateinit var imageAdapter: MovieAdapter
    private lateinit var movieAdapter: MovieAdapter
    private val args: MovieFragmentArgs by navArgs()
    private var list: List<Result>? = null
    private var imageList: List<Poster>? = null
    private lateinit var viewModel: MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Constants.isOnline(requireActivity())) {
            setViews(args)
        } else {
            Toast.makeText(
                activity,
                "You don't have internet connection to proceed",
                Toast.LENGTH_SHORT
            ).show()
            nsv.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setViews(args: MovieFragmentArgs) {
        val movie = args.movie
        ivMoviePoster.load(URLFORIMAGE + movie.poster_path) {
            placeholder(R.drawable.icons8_movie)
        }
        ivBgMovie.load(URLFORIMAGE + movie.poster_path) {
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
        val repository = MovieRepository()
        val viewModelFactory = MovieViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieViewModel::class.java)
        viewModel.getDetails(id)
        viewModel.movieDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { movieResponse ->
                        val df = DecimalFormat("0.0")
                        tvMovieTitle.text = movieResponse.title
                        tvReleaseDate.text = "Release Date - " + movieResponse.release_date
                        tvMovieDesc.text = "Overview - " + movieResponse.overview
                        tvMovieVote.text =
                            "Rating - " + df.format(movieResponse.vote_average).toString()
                        tvRuntime.text = "Runtime - " + movieResponse.runtime + " min"
                        var s = ""
                        for (i in movieResponse.genres!!.indices) {
                            s += movieResponse.genres[i].name
                            if (i != movieResponse.genres.size - 1) {
                                s += ", "
                            }
                        }
                        tvGenre.text = "Genre - $s"
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        Toast.makeText(
                            requireContext(),
                            "An Error occurred $it",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                is Resources.Loading -> {
                }
            }
        }
        viewModel.getSimilar(id)
        viewModel.similarMovieLs.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { movieResponse ->
                        list = movieResponse.results
                        setupRecyclerView()
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        Toast.makeText(
                            requireContext(),
                            "An Error occurred $it",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                is Resources.Loading -> {
                }
            }
        }
        viewModel.getImages(id)
        viewModel.imagesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { imageResponse ->
                        imageList = imageResponse.posters
                        setUpRecyclerView()
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        Toast.makeText(
                            requireContext(),
                            "An Error occurred $it",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                is Resources.Loading -> {
                }
            }
        }

    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(list, imageList, recyclerViewType = SIMILARRECYCLERVIEW)
        rvSimilar.apply {
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
        imageAdapter = MovieAdapter(list, imageList, recyclerViewType = IMAGERECYCLERVIEW)
        rv_movies_images.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = imageAdapter
        }
    }
}