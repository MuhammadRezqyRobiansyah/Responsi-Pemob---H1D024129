package com.film.robiansyah.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.film.robiansyah.data.model.Show
import com.film.robiansyah.data.repository.TvShowRepository
import com.film.robiansyah.data.repository.TvShowRepositoryImpl
import com.film.robiansyah.util.TvMazeConstants.DEFAULT_SEARCH_QUERY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TvShowViewModel(
    private val repository: TvShowRepository = TvShowRepositoryImpl()
) : ViewModel() {

    // State 1: Query pencarian aktif
    private val _searchQuery = MutableStateFlow(DEFAULT_SEARCH_QUERY)
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // State 2: UI State untuk list pencarian film (Loading, Success, Empty, Error)
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    // State 3: UI State untuk halaman detail film
    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    // Cache daftar film yang sedang aktif untuk akses instan di detail screen
    private var cachedShows: List<Show> = emptyList()

    init {
        // Melakukan fetch awal menggunakan default query agar aplikasi langsung memiliki data menarik saat dibuka
        searchShows(DEFAULT_SEARCH_QUERY)
    }

    // Mengubah nilai teks pada search bar
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // Melakukan pencarian ke TVmaze API melalui Repository
    fun searchShows(query: String = _searchQuery.value) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            _searchUiState.value = SearchUiState.Idle
            return
        }

        viewModelScope.launch {
            _searchUiState.value = SearchUiState.Loading

            repository.searchShows(trimmedQuery)
                .onSuccess { shows ->
                    if (shows.isEmpty()) {
                        _searchUiState.value = SearchUiState.Empty
                    } else {
                        cachedShows = shows
                        _searchUiState.value = SearchUiState.Success(shows)
                    }
                }
                .onFailure { error ->
                    _searchUiState.value = SearchUiState.Error(
                        message = error.localizedMessage ?: "Gagal memuat film. Periksa koneksi internet."
                    )
                }
        }
    }

    // Mengambil data detail film berdasarkan ID
    fun loadShowDetail(showId: Int) {
        _detailUiState.value = DetailUiState.Loading

        // Cek terlebih dahulu di cache hasil pencarian untuk respon UI instan tanpa jeda
        val cachedShow = cachedShows.find { it.id == showId }
        if (cachedShow != null) {
            _detailUiState.value = DetailUiState.Success(cachedShow)
        }

        // Ambil data detail terbaru dan terlengkap dari API
        viewModelScope.launch {
            repository.getShowDetail(showId)
                .onSuccess { freshShow ->
                    _detailUiState.value = DetailUiState.Success(freshShow)
                }
                .onFailure { error ->
                    // Jika belum ada di cache dan API gagal, tampilkan error
                    if (cachedShow == null) {
                        _detailUiState.value = DetailUiState.Error(
                            message = error.localizedMessage ?: "Gagal memuat detail film."
                        )
                    }
                }
        }
    }

    // Reset pencarian
    fun clearSearch() {
        _searchQuery.value = ""
        _searchUiState.value = SearchUiState.Idle
    }
}
