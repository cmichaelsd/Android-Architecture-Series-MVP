package com.colemichaels.mvp_demo.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colemichaels.mvp_demo.BaseActivity
import com.colemichaels.mvp_demo.R
import com.colemichaels.mvp_demo.add.AddMovieActivity
import com.colemichaels.mvp_demo.model.LocalDataSource
import com.colemichaels.mvp_demo.model.Movie
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseActivity(), MainContract.ViewInterface {
    companion object {
        private const val TAG = "MainActivity"
        private const val ADD_MOVIE_ACTIVITY_REQUEST_CODE = 1
    }

    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var noMoviesLayout: TextView
    private lateinit var mainPresenter: MainContract.PresenterInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPresenter()
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        mainPresenter.getMyMoviesList()
    }

    override fun onStop() {
        super.onStop()
        mainPresenter.stop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MOVIE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            displayMessage("Movie successfully added.")
        } else {
            displayError("Movie could not be added.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteMenuItem) {
            mainPresenter.onDelete(adapter.selectedMovies)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun displayMovies(moviesList: List<Movie>) {
        adapter.movieList = moviesList
        adapter.notifyDataSetChanged()
        moviesRecyclerView.visibility = View.VISIBLE
        noMoviesLayout.visibility = View.INVISIBLE
    }

    override fun displayNoMovies() {
        Log.d(TAG, "No Movies to display.")
        moviesRecyclerView.visibility = View.INVISIBLE
        noMoviesLayout.visibility = View.VISIBLE
    }

    override fun displayMessage(message: String) {
        showToast(message)
    }

    override fun displayError(message: String) {
        showToast(message)
    }

    private fun setupPresenter() {
        val dataSource = LocalDataSource(application)
        mainPresenter = MainPresenter(this, dataSource)
    }

    private fun setupViews() {
        moviesRecyclerView = findViewById(R.id.activity_main_recyclerview)
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf(), this)
        moviesRecyclerView.adapter = adapter
        fab = findViewById(R.id.activity_main_fab)
        noMoviesLayout = findViewById(R.id.activity_main_no_movies_layout)
        supportActionBar?.title = "Movies to Watch"
    }

    fun goToAddMovieActivity(v: View) {
        val intent = Intent(this, AddMovieActivity::class.java)
        startActivityForResult(intent, ADD_MOVIE_ACTIVITY_REQUEST_CODE)
    }
}