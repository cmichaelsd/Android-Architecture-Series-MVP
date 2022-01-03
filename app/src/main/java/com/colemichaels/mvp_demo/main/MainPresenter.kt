package com.colemichaels.mvp_demo.main

import android.util.Log
import com.colemichaels.mvp_demo.model.LocalDataSource
import com.colemichaels.mvp_demo.model.Movie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class MainPresenter(
    private val viewInterface: MainContract.ViewInterface,
    private val dataSource: LocalDataSource
) : MainContract.PresenterInterface {
    companion object {
        const val TAG = "MainPresenter"
    }

    private val compositeDisposable = CompositeDisposable()
    private val myMoviesObservable: Observable<List<Movie>> get() = dataSource.allMovies

    override fun getMyMoviesList() {
        val myMoviesDisposable = myMoviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<Movie>>() {
                override fun onNext(t: List<Movie>) {
                    if (t.isEmpty()) viewInterface.displayNoMovies()
                    else viewInterface.displayMovies(t)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "Error fetching movie list", e)
                    viewInterface.displayError("Error fetching movie list")
                }

                override fun onComplete() {
                    //Log.d(TAG, "Completed")
                }
            })
        compositeDisposable.add(myMoviesDisposable)
    }

    override fun onDelete(selectMovies: HashSet<Movie>) {
        for (movie in selectMovies) dataSource.delete(movie)
        if (selectMovies.size == 1) viewInterface.displayMessage("Movie deleted.")
        else if (selectMovies.size > 1) viewInterface.displayMessage("Movies deleted.")
    }

    override fun stop() {
        compositeDisposable.clear()
    }
}