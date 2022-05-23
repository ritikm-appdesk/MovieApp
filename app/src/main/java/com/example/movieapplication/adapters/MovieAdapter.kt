package com.example.movieapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.BlurTransformation
import com.example.movieapplication.databinding.MoviesImagesItemsBinding
import com.example.movieapplication.databinding.MoviesListItemsBinding
import com.example.movieapplication.databinding.SimilarMoviesItemsBinding
import com.example.movieapplication.models.Result
import com.example.movieapplication.models.images.Poster
import com.example.movieapplication.utils.Constants.Companion.IMAGERV
import com.example.movieapplication.utils.Constants.Companion.POPULARRV
import com.example.movieapplication.utils.Constants.Companion.SIMILARRV
import com.example.movieapplication.utils.Constants.Companion.URLIMAGE

class MovieAdapter(private val ls :List<Result>?,private val lsi:List<Poster>?,private val type:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewMovieListHolder(val binding : MoviesListItemsBinding) :RecyclerView.ViewHolder(binding.root)
    class ViewSimilarHolder( val binding : SimilarMoviesItemsBinding) :RecyclerView.ViewHolder(binding.root)
    class ViewImagesHolder( val binding : MoviesImagesItemsBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1->ViewMovieListHolder(MoviesListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            2->ViewSimilarHolder(SimilarMoviesItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            3->ViewImagesHolder(MoviesImagesItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> ViewSimilarHolder(SimilarMoviesItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.javaClass) {
            ViewMovieListHolder::class.java -> {
                val movie = ls!!.get(position)
                val viewHolder = holder as ViewMovieListHolder
                viewHolder.binding.apply {
                    ivPoster.load(URLIMAGE +movie.poster_path)
                    ivCardbg.load(URLIMAGE +movie.poster_path){
                        transformations(BlurTransformation(holder.itemView.context,25f))
                    }
                    idTitle.text = movie.title
                    root.setOnClickListener {
                        mlistener?.let {
                            it(movie)
                        }
                    }
                }
            }
            ViewSimilarHolder::class.java -> {
                val viewHolder = holder as ViewSimilarHolder
                val movie = ls!!.get(position)
                viewHolder.binding.apply {
                    ivPoster.load(URLIMAGE + movie.poster_path)
                    ivCardbg.load(URLIMAGE + movie.poster_path) {
                        transformations(BlurTransformation(holder.itemView.context, 25f))
                    }
                    idTitle.text = movie.title
                    root.setOnClickListener {
                        mlistener?.let {
                            it(movie)
                        }
                    }
                }
            }
            ViewImagesHolder::class.java -> {
                val image = lsi!!.get(position)
                val viewHolder = holder as ViewImagesHolder
                viewHolder.binding.apply {
                    ivImages.load(URLIMAGE+image.file_path.toString())
                    Log.d("TAGet",URLIMAGE+ image.file_path.toString())
                }
            }
        }

    }
    private var mlistener : ((Result) -> Unit)? = null

    fun setOnItemCLickListener(listener : (Result)->Unit){
        mlistener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            POPULARRV -> 1
            SIMILARRV -> 2
            IMAGERV -> 3
            else -> 0
        }
    }
    override fun getItemCount(): Int {
        return when (type) {
            POPULARRV -> {
                ls?.let{
                    return it.size
                }
                return 0
            }
            SIMILARRV -> {
                ls?.let{
                    return it.size
                }
                return 0
            }
            IMAGERV -> {
                lsi?.let {
                    return it.size
                }
                return 0
            }
            else -> 0
        }
    }
}