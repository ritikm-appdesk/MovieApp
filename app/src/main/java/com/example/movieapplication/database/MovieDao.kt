package com.example.movieapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movieapplication.models.Result

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    suspend fun insert(movie: Result)

    @Delete
    @Transaction
    suspend fun delete(movie: Result)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Result>>

    @Update
    fun update(movie: Result)
}