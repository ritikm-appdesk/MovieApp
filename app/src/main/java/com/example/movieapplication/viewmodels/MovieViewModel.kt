package com.example.movieapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.complete.newsreporter.utils.Resources
import com.example.movieapplication.database.MovieRepository
import com.example.movieapplication.models.MovieResponse
import com.example.movieapplication.models.Result
import com.example.movieapplication.models.details.MovieDetailsResponse
import com.example.movieapplication.models.images.ImagesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieViewModel(val repo : MovieRepository):ViewModel() {
    var popularMovieLs : MutableLiveData<Resources<MovieResponse>> = MutableLiveData()
    var popularPage = 1
    var popularResponse :MovieResponse? = null
    var similarMovieLs : MutableLiveData<Resources<MovieResponse>> = MutableLiveData()
    var topRatedMovieLs : MutableLiveData<Resources<MovieResponse>> = MutableLiveData()
    var topRatedPage = 1
    var topRatedResponse : MovieResponse? = null
    var searchDetails : MutableLiveData<Resources<MovieResponse>> = MutableLiveData()
    var searchPage = 1
    var searchResponse :MovieResponse? = null
    var movieDetails : MutableLiveData<Resources<MovieDetailsResponse>> = MutableLiveData()
    var imagesResponse : MutableLiveData<Resources<ImagesResponse>> = MutableLiveData()
    var genreReponse : MutableLiveData<Resources<MovieResponse>> = MutableLiveData()

    fun getPopular() = viewModelScope.launch(Dispatchers.IO) {
        popularMovieLs.postValue(Resources.Loading())
        val pm = repo.popularMovies(popularPage)
        popularMovieLs.postValue(handlePopularReponse(pm))
    }
    private fun handlePopularReponse(response: Response<MovieResponse>):Resources<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                if(popularResponse == null){
                    popularResponse = resultResponse
                }else{
                    val oldList :MutableList<Result>? = popularResponse!!.results
                    val newList :MutableList<Result>? = resultResponse.results
                    oldList!!.addAll(newList!!)
                }
                return Resources.Success(popularResponse!!)
            }
        }
        return Resources.Error(response.message())
    }
    fun getSimilar(id:Int) = viewModelScope.launch(Dispatchers.IO) {
        similarMovieLs.postValue(Resources.Loading())
        val pm = repo.getSimilar(id)
        similarMovieLs.postValue(handleSimilarReponse(pm))
    }
    private fun handleSimilarReponse(response: Response<MovieResponse>):Resources<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
    fun getTopRated() = viewModelScope.launch(Dispatchers.IO) {
        topRatedMovieLs.postValue(Resources.Loading())
        val pm = repo.topRatedMovies(topRatedPage)
        topRatedMovieLs.postValue(handleTopRatedReponse(pm))
    }
    private fun handleTopRatedReponse(response: Response<MovieResponse>):Resources<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                if(topRatedResponse == null){
                    topRatedResponse = resultResponse
                }else{
                    val oldList :MutableList<Result>? = topRatedResponse!!.results
                    val newList :MutableList<Result>? = resultResponse.results
                    oldList!!.addAll(newList!!)
                }
                return Resources.Success(topRatedResponse!!)
            }
        }
        return Resources.Error(response.message())
    }
    fun getSearch(query:String) = viewModelScope.launch {
        searchDetails.postValue(Resources.Loading())
        val pm = repo.getSearchDetails(query,searchPage)
        searchDetails.postValue(handleSearchReponse(pm))
    }
    private fun handleSearchReponse(response: Response<MovieResponse>):Resources<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                if(searchPage == 1){
                    searchResponse = resultResponse
                }else{
                    val oldList :MutableList<Result>? = searchResponse!!.results
                    val newList :MutableList<Result>? = resultResponse.results
                    oldList!!.addAll(newList!!)
                }
                return Resources.Success(searchResponse!!)
            }
        }
        return Resources.Error(response.message())
    }
    fun getDetails(id:Int) = viewModelScope.launch(Dispatchers.IO) {
        movieDetails.postValue(Resources.Loading())
        val pm = repo.getDetails(id)
        movieDetails.postValue(handleDetailsReponse(pm))
    }
    private fun handleDetailsReponse(response: Response<MovieDetailsResponse>):Resources<MovieDetailsResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
    fun getImages(id:Int) = viewModelScope.launch(Dispatchers.IO) {
        imagesResponse.postValue(Resources.Loading())
        val pm = repo.getImages(id)
        imagesResponse.postValue(handleImageReponse(pm))
    }
    private fun handleImageReponse(response: Response<ImagesResponse>):Resources<ImagesResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
    fun getMovieWithGenre(id:Int) = viewModelScope.launch(Dispatchers.IO) {
        genreReponse.postValue(Resources.Loading())
        val pm = repo.getMovieWithGenre(id)
        genreReponse.postValue(handleGenreReponse(pm))
    }
    private fun handleGenreReponse(response: Response<MovieResponse>):Resources<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }


}