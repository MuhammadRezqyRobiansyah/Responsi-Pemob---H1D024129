package com.film.robiansyah.data.repository

import com.film.robiansyah.data.model.Show

interface TvShowRepository {
    // Mencari film / serial TV berdasarkan kata kunci dan mengembalikan List<Show>
    suspend fun searchShows(query: String): Result<List<Show>>

    // Mengambil data detail satu film / serial TV berdasarkan ID
    suspend fun getShowDetail(id: Int): Result<Show>
}
