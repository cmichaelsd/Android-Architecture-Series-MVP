package com.colemichaels.mvp_demo.model.main

import com.colemichaels.mvp_demo.main.MainContract
import com.colemichaels.mvp_demo.main.MainPresenter
import com.colemichaels.mvp_demo.model.BaseTest
import com.colemichaels.mvp_demo.model.LocalDataSource
import com.colemichaels.mvp_demo.model.Movie
import com.colemichaels.mvp_demo.model.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.collections.ArrayList

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTests : BaseTest() {
    @Rule
    @JvmField
    var testSchedulers = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: MainContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: LocalDataSource

    lateinit var mainPresenter: MainPresenter

    private val dummyAllMovies: ArrayList<Movie> get() = ArrayList<Movie>().apply {
        add(Movie(title = "Title1", releaseDate = "ReleaseDate1", posterPath = "PosterPath1"))
        add(Movie(title = "Title2", releaseDate = "ReleaseDate2", posterPath = "PosterPath2"))
        add(Movie(title = "Title3", releaseDate = "ReleaseDate3", posterPath = "PosterPath3"))
        add(Movie(title = "Title4", releaseDate = "ReleaseDate4", posterPath = "PosterPath4"))
    }

    private val deleteHashSetSingle: HashSet<Movie> get() = HashSet<Movie>().apply {
        add(dummyAllMovies[2])
    }

    private val deleteHashSetMultiple: HashSet<Movie> get() = HashSet<Movie>().apply {
        add(dummyAllMovies[1])
        add(dummyAllMovies[3])
    }

    @Before
    fun setUp() {
        mainPresenter = MainPresenter(viewInterface = mockActivity, dataSource = mockDataSource)
    }

    @Test
    fun testGetMyMoviesList() {
        val myDummyMovies = dummyAllMovies
        Mockito.doReturn(Observable.just(myDummyMovies)).`when`(mockDataSource).allMovies

        mainPresenter.getMyMoviesList()

        Mockito.verify(mockDataSource).allMovies
        Mockito.verify(mockActivity).displayMovies(myDummyMovies)
    }

    @Test
    fun testGetMyMoviesListWithNoMovies() {
        Mockito.doReturn(Observable.just(ArrayList<Movie>())).`when`(mockDataSource).allMovies

        mainPresenter.getMyMoviesList()

        Mockito.verify(mockDataSource).allMovies
        Mockito.verify(mockActivity).displayNoMovies()
    }

    @Test
    fun testDeleteSingle() {
        val myDeletedHashSet = deleteHashSetSingle
        mainPresenter.onDelete(myDeletedHashSet)

        for (movie in myDeletedHashSet) {
            Mockito.verify(mockDataSource).delete(movie)
        }

        Mockito.verify(mockActivity).displayMessage("Movie deleted.")
    }

    @Test
    fun testDeleteMultiple() {
        val myDeletedHashSet = deleteHashSetMultiple
        mainPresenter.onDelete(myDeletedHashSet)

        for (movie in myDeletedHashSet) {
            Mockito.verify(mockDataSource).delete(movie)
        }

        Mockito.verify(mockActivity).displayMessage("Movies deleted.")
    }
}