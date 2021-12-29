package com.colemichaels.mvp_demo.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.colemichaels.mvp_demo.BaseActivity
import com.colemichaels.mvp_demo.Keys
import com.colemichaels.mvp_demo.R
import com.colemichaels.mvp_demo.model.LocalDataSource
import com.colemichaels.mvp_demo.search.SearchActivity

class AddMovieActivity : BaseActivity(), AddMovieContract.ViewInterface {
    companion object {
        private const val SEARCH_MOVIE_ACTIVITY_REQUEST_CODE = 2
    }

    private lateinit var titleEditText: EditText
    private lateinit var releaseDateEditText: EditText
    private lateinit var movieImageView: ImageView
    private lateinit var addMoviePresenter: AddMovieContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)
        setupPresenter()
        setupViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        runOnUiThread {
            titleEditText.setText(data?.getStringExtra(Keys.EXTRA_TITLE))
            releaseDateEditText.setText(data?.getStringExtra(Keys.EXTRA_RELEASE_DATE))
            movieImageView.tag = data?.getStringExtra(Keys.EXTRA_POSTER_PATH)
            Glide.with(this)
                .load(data?.getStringExtra(Keys.EXTRA_POSTER_PATH))
                .override(Target.SIZE_ORIGINAL)
                .into(movieImageView)
        }
    }

    override fun returnToMain() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun displayMessage(message: String) {
        showToast(message)
    }

    override fun displayError(message: String) {
        showToast(message)
    }

    private fun setupPresenter() {
        val dataSource = LocalDataSource(application)
        addMoviePresenter = AddMoviePresenter(this, dataSource)
    }

    private fun setupViews() {
        titleEditText = findViewById(R.id.activity_add_movie_title)
        releaseDateEditText = findViewById(R.id.activity_add_movie_release_year)
        movieImageView = findViewById(R.id.activity_add_movie_imageview)
    }

    fun goToSearchMovieActivity(v: View) {
        val title = titleEditText.text.toString()
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(Keys.SEARCH_QUERY, title)
        startActivityForResult(intent, SEARCH_MOVIE_ACTIVITY_REQUEST_CODE)
    }

    fun onClickAddMovie(v: View) {
        val title = titleEditText.text.toString()
        val releaseDate = releaseDateEditText.text.toString()
        val posterPath = if (movieImageView.tag != null) movieImageView.tag.toString() else ""
        addMoviePresenter.addMovie(title, releaseDate, posterPath)
    }
}