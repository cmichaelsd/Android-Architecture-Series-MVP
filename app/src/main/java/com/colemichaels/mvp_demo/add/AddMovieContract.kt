package com.colemichaels.mvp_demo.add

class AddMovieContract {
    interface PresenterInterface {
        fun addMovie(title: String, releaseDate: String, postPath: String)
    }

    interface ViewInterface {
        fun returnToMain()
        fun displayMessage(message: String)
        fun displayError(message: String)
    }
}