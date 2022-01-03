package com.colemichaels.mvp_demo.model.search

import com.colemichaels.mvp_demo.model.*
import com.colemichaels.mvp_demo.search.SearchContract
import com.colemichaels.mvp_demo.search.SearchPresenter
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.collections.ArrayList

@RunWith(MockitoJUnitRunner::class)
class SearchPresenterTests : BaseTest() {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: SearchContract.ViewInterface

    @Mock
    private val mockDataSource = RemoteDataSource()

    lateinit var searchPresenter: SearchPresenter

    private val dummyResponse: TmdbResponse get() = TmdbResponse(
        1,
        4,
        5,
        ArrayList<Movie>().apply {
            add(Movie(title = "Title1", releaseDate = "ReleaseDate1", posterPath = "PosterPath1"))
            add(Movie(title = "Title2", releaseDate = "ReleaseDate2", posterPath = "PosterPath2"))
            add(Movie(title = "Title3", releaseDate = "ReleaseDate3", posterPath = "PosterPath3"))
            add(Movie(title = "Title4", releaseDate = "ReleaseDate4", posterPath = "PosterPath4"))
    })

    @Before
    fun setUp() {
        searchPresenter = SearchPresenter(mockActivity, mockDataSource)
    }

    @Test
    fun testSearchMovieError() {
        Mockito.doReturn(Observable.error<Throwable>(Throwable("Something went wrong")))
            .`when`(mockDataSource).searchResultsObservable(anyString())

        searchPresenter.getSearchResults("The Lion King")

        Mockito.verify(mockActivity).displayError("Error fetching movie data.")
    }

    @Test
    fun testSearchMovie() {
        val myDummyResponse = dummyResponse
        Mockito.doReturn(Observable.just(myDummyResponse)).`when`(mockDataSource).searchResultsObservable(anyString())

        searchPresenter.getSearchResults("The Lion King")

        Mockito.verify(mockActivity).displayResult(myDummyResponse)
    }
}