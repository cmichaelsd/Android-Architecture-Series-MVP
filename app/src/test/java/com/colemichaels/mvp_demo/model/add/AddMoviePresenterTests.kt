package com.colemichaels.mvp_demo.model.add

import com.colemichaels.mvp_demo.add.AddMovieContract
import com.colemichaels.mvp_demo.add.AddMoviePresenter
import com.colemichaels.mvp_demo.model.BaseTest
import com.colemichaels.mvp_demo.model.LocalDataSource
import com.colemichaels.mvp_demo.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddMoviePresenterTests : BaseTest() {
    @Mock
    private lateinit var mockActivity: AddMovieContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: LocalDataSource

    @Captor
    private lateinit var movieArgumentCaptor: ArgumentCaptor<Movie>

    lateinit var addMoviePresenter: AddMoviePresenter

    @Before
    fun setUp() {
        addMoviePresenter = AddMoviePresenter(mockActivity, mockDataSource)
    }

    @Test
    fun testAddMovieNoTitle() {
        addMoviePresenter.addMovie("", "" ,"")
        Mockito.verify(mockActivity).displayError("Movie title can not be blank.")
    }

    @Test
    fun testAddMovieWithTitle() {
        addMoviePresenter.addMovie(
            "The Lion King",
            "1994-05-07",
            "/bKPtXn9n4M4s8vvZrbw40mYsefB.jpg"
        )

        Mockito.verify(mockDataSource).insert(captureArg(movieArgumentCaptor))

        assertEquals("The Lion King", movieArgumentCaptor.value.title)

        Mockito.verify(mockActivity).returnToMain()
    }
}