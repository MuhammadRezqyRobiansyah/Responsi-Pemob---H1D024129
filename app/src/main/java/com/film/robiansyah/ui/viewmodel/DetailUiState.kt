package com.film.robiansyah.ui.viewmodel

import com.film.robiansyah.data.model.Show

// Sealed interface untuk mengelola status UI pada halaman Detail
sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val show: Show) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
