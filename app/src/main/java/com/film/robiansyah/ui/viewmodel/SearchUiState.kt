package com.film.robiansyah.ui.viewmodel

import com.film.robiansyah.data.model.Show

// Sealed interface untuk mengelola status UI pada halaman pencarian/Home
sealed interface SearchUiState {
    object Idle : SearchUiState
    object Loading : SearchUiState
    data class Success(val shows: List<Show>) : SearchUiState
    object Empty : SearchUiState
    data class Error(val message: String) : SearchUiState
}
