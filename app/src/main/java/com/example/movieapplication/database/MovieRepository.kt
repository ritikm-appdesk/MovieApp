package com.example.movieapplication.database

import com.example.movieapplication.api.RetrofitInstance

class MovieRepository(val db : MovieDatabase) {

    suspend fun  popularMovies(page:Int) = RetrofitInstance.api.getPopular(page = page)
    suspend fun  topRatedMovies(page:Int) = RetrofitInstance.api.getTopRated(page = page)
    suspend fun getSimilar(id:Int) = RetrofitInstance.api.getSimilar(movieId = id)
    suspend fun getSearchDetails(query:String,page:Int) = RetrofitInstance.api.getSearched(query = query, page = page)
    suspend fun getDetails(id :Int) = RetrofitInstance.api.getDetails(movieId = id)
    suspend fun getImages(id:Int) = RetrofitInstance.api.getImages(movieId = id)
    suspend fun getMovieWithGenre(id:Int) = RetrofitInstance.api.getMovieViaGenre(genre = id)

}