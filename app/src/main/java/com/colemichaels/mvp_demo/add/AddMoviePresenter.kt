package com.colemichaels.mvp_demo.add

import com.colemichaels.mvp_demo.model.LocalDataSource
import com.colemichaels.mvp_demo.model.Movie

class AddMoviePresenter(
    private val viewInterface: AddMovieContract.ViewInterface,
    private val dataSource: LocalDataSource
) : AddMovieContract.PresenterInterface {
    override fun addMovie(title: String, releaseDate: String, postPath: String) {
        if (title.isBlank()) viewInterface.displayError("Movie title can not be blank.")
        else {
            val movie = Movie(title = title, releaseDate = releaseDate, posterPath = postPath)
            dataSource.insert(movie)
            viewInterface.returnToMain()
        }
    }
}