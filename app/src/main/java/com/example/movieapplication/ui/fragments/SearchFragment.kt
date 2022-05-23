package com.example.movieapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.complete.newsreporter.utils.Resources
import com.example.movieapplication.R
import com.example.movieapplication.adapters.MovieAdapter
import com.example.movieapplication.database.MovieDatabase
import com.example.movieapplication.database.MovieRepository
import com.example.movieapplication.models.Result
import com.example.movieapplication.utils.Constants
import com.example.movieapplication.viewmodels.MovieViewModel
import com.example.movieapplication.viewmodels.MovieViewModelFactory
import kotlinx.android.synthetic.main.fragment_popular_movie.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel

    var isScrolling = false
    var position = 0
    var searched :String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel()
        var job : Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchPage = 1
                        viewModel.getSearch(editable.toString())
                        searched = editable.toString()
                    }
                }
            }
        }
    }

    private fun viewModel(){
        val repository  = MovieRepository(MovieDatabase(requireActivity()))
        val viewModelFactory = MovieViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(MovieViewModel::class.java)

        viewModel.searchDetails.observe(viewLifecycleOwner, Observer{ response->
            when(response){
                is Resources.Success ->{
                    response.data?.let {movieResponse ->
                        hideProgress()
                        setupRV(movieResponse.results!!.toList())
                    }
                }
                is Resources.Error ->{
                    response.data?.let{
                        hideProgress()
                        Toast.makeText(requireContext(),"An Error occured $it", Toast.LENGTH_SHORT).show()
                        Log.d(Constants.TAG,"Something is wrong")
                    }
                }
                is Resources.Loading ->{
                    showProgress()
                }
            }
        })
    }
    val onScroll = object : RecyclerView.OnScrollListener() {
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
                viewModel.getSearch(searched!!)
                isScrolling = false
                viewModel.searchPage++
                position = totalItems-(currentItems/2)
            }
        }
    }
    private fun setupRV(ls:List<Result>){
        movieAdapter = MovieAdapter(ls = ls, null,type = Constants.POPULARRV)
        rv_search_result.apply {
            layoutManager = GridLayoutManager(activity,2)
            adapter = movieAdapter
            addOnScrollListener(onScroll)
            scrollToPosition(position)
        }
        movieAdapter.setOnItemCLickListener{
            val bundle = Bundle().apply {
                putSerializable("movie",it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_movieFragment,bundle)
        }
    }
    private fun showProgress(){
        progressSearch.visibility = View.VISIBLE
    }
    private fun hideProgress(){
        progressSearch.visibility = View.GONE
    }
}