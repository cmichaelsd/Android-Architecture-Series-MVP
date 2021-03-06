package com.colemichaels.mvp_demo.model

import android.app.Application
import io.reactivex.rxjava3.core.Observable
import kotlin.concurrent.thread

open class LocalDataSource(application: Application) {
    private val movieDao: MovieDao
    open val allMovies: Observable<List<Movie>>

    init {
        val db = LocalDatabase.getInstance(application)
        movieDao = db.movieDao()
        allMovies = movieDao.all
    }

    open fun insert(movie: Movie) {
        thread {
            movieDao.insert(movie)
        }
    }

    open fun delete(movie: Movie) {
        thread {
            movieDao.delete(movie.id)
        }
    }

    open fun update(movie: Movie) {
        thread {
            movieDao.update(movie)
        }
    }
}