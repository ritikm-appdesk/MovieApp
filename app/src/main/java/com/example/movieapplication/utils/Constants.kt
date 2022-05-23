package com.example.movieapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.movieapplication.models.details.Genre

class Constants {
    var genres = HashMap<Int,String>()
    
    companion object{
        const val TAG = "TAG"
        const val APIKEY = "065cfe7d61ef3459da51f59d9c8c470c"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val URLIMAGE = "https://image.tmdb.org/t/p/original"
        const val IMAGERV = "IMAGERV"
        const val SIMILARRV = "SIMILARRV"
        const val POPULARRV = "POPULARRV"
        const val PAGE_SIZE = 20


        val arrayGenre = arrayListOf<Genre>(Genre(27,"horror"),Genre(53,"thriller"),
            Genre(878,"sci-fi"),Genre(12,"adventure"),Genre(10749,"romance"))


        @RequiresApi(Build.VERSION_CODES.M)
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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