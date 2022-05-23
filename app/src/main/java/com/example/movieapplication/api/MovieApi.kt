package com.example.movieapplication.api

import com.example.movieapplication.models.MovieResponse
import com.example.movieapplication.models.details.MovieDetailsResponse
import com.example.movieapplication.models.images.ImagesResponse
import com.example.movieapplication.utils.Constants.Companion.APIKEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String = APIKEY,
        //@Query("language") language : String = "en-US",
        @Query("page") page: Int,
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String = APIKEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilar(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = APIKEY,
        @Query("language") language: String = "en",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun getSearched(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = APIKEY,
        @Query("language") language: String = "en",
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = APIKEY,
        @Query("language") language: String = "en"
    ): Response<MovieDetailsResponse>

    @GET("movie/{movie_id}/images")
    suspend fun getImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = APIKEY,
        @Query("language") language: String = "en"
    ): Response<ImagesResponse>

    @GET("discover/movie")
    suspend fun getMovieViaGenre(
        @Query("api_key") apiKey: String = APIKEY,
        @Query("language") language: String = "en",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") genre: Int

    ): Response<MovieResponse>

}