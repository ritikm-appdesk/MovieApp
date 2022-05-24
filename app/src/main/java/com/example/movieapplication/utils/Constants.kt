package com.example.movieapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.movieapplication.models.details.Genre

class Constants {
    companion object {
        const val APIKEY = "065cfe7d61ef3459da51f59d9c8c470c"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val URLFORIMAGE = "https://image.tmdb.org/t/p/original"
        const val IMAGERECYCLERVIEW = "IMAGERV"
        const val SIMILARRECYCLERVIEW = "SIMILARRV"
        const val POPULARRECYCLERVIEW = "POPULARRV"


        val arrayGenre = arrayListOf(
            Genre(27, "horror"), Genre(53, "thriller"),
            Genre(878, "sci-fi"), Genre(12, "adventure"), Genre(10749, "romance")
        )

        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                }else{
                    connectivityManager.getNetworkCapabilities(null)
                }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
            return false
        }
    }
}