package com.colemichaels.mvp_demo.search

import com.colemichaels.mvp_demo.model.TmdbResponse

class SearchContract {
    interface PresenterInterface {
        fun getSearchResults(query: String)
        fun stop()
    }

    interface ViewInterface {
        fun displayResult(tmdbResponse: TmdbResponse)
        fun displayMessage(message: String)
        fun displayError(message: String)
    }
}