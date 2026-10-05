package com.film.robiansyah.data.model

import java.util.Locale

data class Show(
    val id: Int,
    val name: String,
    val premiered: String? = null,
    val genres: List<String>? = emptyList(),
    val rating: Rating? = null,
    val summary: String? = null,
    val image: ShowImage? = null,
    val status: String? = null,
    val language: String? = null
) {
    // Ekstraksi 4 digit pertama untuk tahun rilis (contoh: "2021-09-17" -> "2021")
    val releaseYear: String
        get() = premiered?.take(4)?.takeIf { it.isNotBlank() } ?: "TBA"

    // Format tampilan rating
    val ratingText: String
        get() = rating?.average?.let { String.format(Locale.US, "%.1f", it) } ?: "N/A"

    // Format gabungan genre
    val genresText: String
        get() = if (!genres.isNullOrEmpty()) genres.joinToString(", ") else "Umum"

    // Pembersih tag HTML dari summary (karena TVmaze API mengembalikan tag seperti <p>, <b>, dll)
    val cleanSummary: String
        get() {
            if (summary.isNullOrBlank()) return "Deskripsi tidak tersedia."
            return summary
                .replace(Regex("<[^>]*>"), "") // Hapus semua tag HTML <...>
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&nbsp;", " ")
                .trim()
        }
}
