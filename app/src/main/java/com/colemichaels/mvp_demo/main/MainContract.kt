package com.colemichaels.mvp_demo.main

import com.colemichaels.mvp_demo.model.Movie

class MainContract {
    interface PresenterInterface {
        fun getMyMoviesList()
        fun onDelete(selectMovies: HashSet<Movie>)
        fun stop()
    }

    interface ViewInterface {
        fun displayMovies(moviesList: List<Movie>)
        fun displayNoMovies()
        fun displayMessage(message: String)
        fun displayError(message: String)
    }
}