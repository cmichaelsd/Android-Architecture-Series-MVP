package com.colemichaels.mvp_demo.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colemichaels.mvp_demo.BaseActivity
import com.colemichaels.mvp_demo.Keys
import com.colemichaels.mvp_demo.R
import com.colemichaels.mvp_demo.model.RemoteDataSource
import com.colemichaels.mvp_demo.model.TmdbResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchActivity : BaseActivity(), SearchContract.ViewInterface {
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var noMoviesTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchPresenter: SearchPresenter
    private lateinit var query: String

    private val itemListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemClick(v: View, position: Int) {
            val movie = adapter.getItemAtPosition(position)
            val replyIntent = Intent().apply {
                putExtra(Keys.EXTRA_TITLE, movie.title)
                putExtra(Keys.EXTRA_RELEASE_DATE, movie.getReleaseYearFromDate())
                putExtra(Keys.EXTRA_POSTER_PATH, movie.getPosterUrl())
            }

            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchResultsRecyclerView = findViewById(R.id.activity_search_recyclerview)
        noMoviesTextView = findViewById(R.id.activity_search_no_movies_textview)
        progressBar = findViewById(R.id.activity_search_progress_bar)

        val intent = intent
        query = intent.getStringExtra(Keys.SEARCH_QUERY).toString()

        setupPresenter()
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        searchPresenter.getSearchResults(query)
    }

    override fun onStop() {
        super.onStop()
        searchPresenter.stop()
    }

    override fun displayResult(tmdbResponse: TmdbResponse) {
        progressBar.visibility = View.INVISIBLE

        if (tmdbResponse.totalResults == null || tmdbResponse.totalResults == 0) {
            searchResultsRecyclerView.visibility = View.INVISIBLE
            noMoviesTextView.visibility = View.VISIBLE
        } else {
            adapter = SearchAdapter(tmdbResponse.results ?: arrayListOf(), this, itemListener)
            adapter.notifyDataSetChanged()
            searchResultsRecyclerView.adapter = adapter
            noMoviesTextView.visibility = View.INVISIBLE
        }
    }

    override fun displayMessage(message: String) {
        showToast(message)
    }

    override fun displayError(message: String) {
        showToast(message)
    }

    private fun setupPresenter() {
        val dataSource = RemoteDataSource()
        searchPresenter = SearchPresenter(this, dataSource)
    }

    private fun setupViews() {
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    interface RecyclerItemListener {
        fun onItemClick(v: View, position: Int)
    }
}