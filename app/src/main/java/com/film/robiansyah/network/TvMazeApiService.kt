package com.film.robiansyah.network

import com.film.robiansyah.data.model.SearchResultItem
import com.film.robiansyah.data.model.Show
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeApiService {

    // Endpoint pencarian film / serial TV: /search/shows?q={query}
    @GET("search/shows")
    suspend fun searchShows(
        @Query("q") query: String
    ): List<SearchResultItem>

    // Endpoint untuk mengambil detail show berdasarkan ID: /shows/{id}
    @GET("shows/{id}")
    suspend fun getShowDetail(
        @Path("id") id: Int
    ): Show
}
