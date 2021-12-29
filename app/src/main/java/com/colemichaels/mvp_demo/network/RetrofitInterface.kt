package com.colemichaels.mvp_demo.network

import com.colemichaels.mvp_demo.model.TmdbResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("query") q: String
    ): Observable<TmdbResponse>
}