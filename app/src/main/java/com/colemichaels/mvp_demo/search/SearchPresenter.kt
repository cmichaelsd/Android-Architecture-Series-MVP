package com.colemichaels.mvp_demo.search

import android.util.Log
import com.colemichaels.mvp_demo.model.LocalDataSource
import com.colemichaels.mvp_demo.model.RemoteDataSource
import com.colemichaels.mvp_demo.model.TmdbResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchPresenter(
    private val viewInterface: SearchContract.ViewInterface,
    private val dataSource: RemoteDataSource
) : SearchContract.PresenterInterface {
    companion object {
        private const val TAG = "SearchPresenter"
    }

    private val compositeDisposable = CompositeDisposable()

    val searchResultsObservable: (String) -> Observable<TmdbResponse> = {
        dataSource.searchResultsObservable(it)
    }

    override fun getSearchResults(query: String) {val searchResultsDisposable = searchResultsObservable(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(object : DisposableObserver<TmdbResponse>() {
            override fun onNext(t: TmdbResponse) {
                viewInterface.displayResult(t)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                viewInterface.displayError("Error fetching movie data.")
            }

            override fun onComplete() {}
        })

        compositeDisposable.add(searchResultsDisposable)
    }

    override fun stop() {
        compositeDisposable.clear()
    }


}