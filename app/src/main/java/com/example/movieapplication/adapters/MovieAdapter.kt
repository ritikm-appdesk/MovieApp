package com.example.movieapplication.adapters

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
import com.example.movieapplication.utils.Constants.Companion.IMAGERECYCLERVIEW
import com.example.movieapplication.utils.Constants.Companion.POPULARRECYCLERVIEW
import com.example.movieapplication.utils.Constants.Companion.SIMILARRECYCLERVIEW
import com.example.movieapplication.utils.Constants.Companion.URLFORIMAGE

class MovieAdapter(private val list :List<Result>?, private val imageList:List<Poster>?, private val recyclerViewType:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                val movie = list!![position]
                val viewHolder = holder as ViewMovieListHolder
                viewHolder.binding.apply {
                    ivPoster.load(URLFORIMAGE +movie.poster_path)
                    ivCardbg.load(URLFORIMAGE +movie.poster_path){
                        transformations(BlurTransformation(holder.itemView.context,25f))
                    }
                    idTitle.text = movie.title
                    root.setOnClickListener {
                        onCLickListener?.let {
                            it(movie)
                        }
                    }
                }
            }
            ViewSimilarHolder::class.java -> {
                val viewHolder = holder as ViewSimilarHolder
                val movie = list!![position]
                viewHolder.binding.apply {
                    ivPoster.load(URLFORIMAGE + movie.poster_path)
                    ivCardbg.load(URLFORIMAGE + movie.poster_path) {
                        transformations(BlurTransformation(holder.itemView.context, 25f))
                    }
                    idTitle.text = movie.title
                    root.setOnClickListener {
                        onCLickListener?.let {
                            it(movie)
                        }
                    }
                }
            }
            ViewImagesHolder::class.java -> {
                val image = imageList!![position]
                val viewHolder = holder as ViewImagesHolder
                viewHolder.binding.apply {
                    ivImages.load(URLFORIMAGE+ image.file_path)
                }
            }
        }

    }
    private var onCLickListener : ((Result) -> Unit)? = null

    fun setOnItemCLickListener(listener : (Result)->Unit){
        onCLickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return when (recyclerViewType) {
            POPULARRECYCLERVIEW -> 1
            SIMILARRECYCLERVIEW -> 2
            IMAGERECYCLERVIEW -> 3
            else -> 0
        }
    }
    override fun getItemCount(): Int {
        return when (recyclerViewType) {
            POPULARRECYCLERVIEW -> {
                list?.let{
                    return it.size
                }
                return 0
            }
            SIMILARRECYCLERVIEW -> {
                list?.let{
                    return it.size
                }
                return 0
            }
            IMAGERECYCLERVIEW -> {
                imageList?.let {
                    return it.size
                }
                return 0
            }
            else -> 0
        }
    }
}