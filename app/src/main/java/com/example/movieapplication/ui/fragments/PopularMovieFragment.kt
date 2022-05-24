package com.example.movieapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.adapters.MovieAdapter
import com.example.movieapplication.database.MovieRepository
import com.example.movieapplication.models.Result
import com.example.movieapplication.utils.Constants.Companion.POPULARRECYCLERVIEW
import com.example.movieapplication.utils.Constants.Companion.arrayGenre
import com.example.movieapplication.utils.Constants.Companion.isOnline
import com.example.movieapplication.utils.Resources
import com.example.movieapplication.viewmodels.MovieViewModel
import com.example.movieapplication.viewmodels.MovieViewModelFactory
import kotlinx.android.synthetic.main.fragment_popular_movie.*

class PopularMovieFragment : Fragment(R.layout.fragment_popular_movie) {

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    var isScrolling = false
    var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular_movie, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository  = MovieRepository()
        val viewModelFactory = MovieViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(MovieViewModel::class.java)
       if(isOnline(requireActivity())){
           defaultText.visibility = View.GONE
           viewModel()
       }else{
           Toast.makeText(activity,"You don't have internet connection to proceed",Toast.LENGTH_SHORT).show()
           hsv.visibility = View.GONE
           defaultText.visibility = View.VISIBLE
       }
        str.setOnRefreshListener {
            if(isOnline(requireActivity())){
                defaultText.visibility = View.GONE
                viewModel()
            }else{
                Toast.makeText(activity,"You don't have internet connection to proceed",Toast.LENGTH_SHORT).show()
                hsv.visibility = View.GONE
                defaultText.visibility = View.VISIBLE
            }
            str.isRefreshing = false
        }
        genreBar()
    }
    private fun genreBar(){
        horror.setOnClickListener {
            viewModel.getMovieWithGenre(arrayGenre[0].id)
        }
        thriller.setOnClickListener { viewModel.getMovieWithGenre(arrayGenre[1].id)}
        sciFi.setOnClickListener { viewModel.getMovieWithGenre(arrayGenre[2].id)}
        adventure.setOnClickListener { viewModel.getMovieWithGenre(arrayGenre[3].id)}
        romantic.setOnClickListener { viewModel.getMovieWithGenre(arrayGenre[4].id)}

        viewModel.genreResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { movieResponse ->
                        hideProgress()
                        setupRV(movieResponse.results!!)
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        hideProgress()
                        Toast.makeText(
                            requireContext(),
                            "An Error occurred $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resources.Loading -> {
                    showProgress()
                }
            }
        }
    }
    private fun viewModel(){
        viewModel.getPopular()
        viewModel.popularMovieLs.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { movieResponse ->
                        hideProgress()
                        setupRV(movieResponse.results!!)
                    }
                }
                is Resources.Error -> {
                    response.data?.let {
                        hideProgress()
                        Toast.makeText(
                            requireContext(),
                            "An Error occurred $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resources.Loading -> {
                    showProgress()
                }
            }
        }
    }

    private val onScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
            val currentItems = layoutManager.childCount
            val totalItems = layoutManager.itemCount

            if(isScrolling && (currentItems + scrolledOutItems >= totalItems)){
                viewModel.getPopular()
                isScrolling = false
                viewModel.popularPage++
                position = totalItems-(currentItems/2)
            }
        }
    }
    private fun setupRV(list:List<Result>){
        movieAdapter = MovieAdapter(list = list, null, recyclerViewType = POPULARRECYCLERVIEW)
        rvPopular.apply {
            layoutManager = GridLayoutManager(activity,2)
            adapter = movieAdapter
            addOnScrollListener(onScroll)
            scrollToPosition(position)
        }
        movieAdapter.setOnItemCLickListener{
            val bundle = Bundle().apply {
                putSerializable("movie",it)
            }
            findNavController().navigate(R.id.action_popularMovieFragment_to_movieFragment,bundle)
        }
    }
    private fun showProgress(){
        progressPopular.visibility = View.VISIBLE
    }
    private fun hideProgress(){
        progressPopular.visibility = View.GONE
    }

}