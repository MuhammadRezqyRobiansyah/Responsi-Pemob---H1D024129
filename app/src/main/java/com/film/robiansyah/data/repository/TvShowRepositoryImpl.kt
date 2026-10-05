package com.film.robiansyah.data.repository

import com.film.robiansyah.data.model.Show
import com.film.robiansyah.network.ApiClient
import com.film.robiansyah.network.TvMazeApiService

class TvShowRepositoryImpl(
    private val apiService: TvMazeApiService = ApiClient.instance
) : TvShowRepository {

    override suspend fun searchShows(query: String): Result<List<Show>> {
        return try {
            if (query.trim().isEmpty()) {
                return Result.success(emptyList())
            }
            val response = apiService.searchShows(query.trim())
            // Ekstrak objek 'show' dari setiap item 'SearchResultItem'
            val shows = response.map { it.show }
            Result.success(shows)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getShowDetail(id: Int): Result<Show> {
        return try {
            val show = apiService.getShowDetail(id)
            Result.success(show)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
