package com.film.robiansyah.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.film.robiansyah.data.model.Show
import com.film.robiansyah.ui.components.CineSearchBar
import com.film.robiansyah.ui.components.ShowItemCard
import com.film.robiansyah.ui.theme.*
import com.film.robiansyah.ui.viewmodel.SearchUiState
import com.film.robiansyah.ui.viewmodel.TvShowViewModel
import com.film.robiansyah.util.TvMazeConstants.DEFAULT_SEARCH_QUERY

// STATEFUL COMPOSABLE (Menangani ViewModel & Navigasi)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TvShowViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchUiState by viewModel.searchUiState.collectAsState()

    StatelessHomeScreen(
        searchQuery = searchQuery,
        onQueryChange = { viewModel.onSearchQueryChanged(it) },
        onSearch = { viewModel.searchShows() },
        onClearSearch = { viewModel.clearSearch() },
        onResetToDefault = {
            viewModel.onSearchQueryChanged(DEFAULT_SEARCH_QUERY)
            viewModel.searchShows(DEFAULT_SEARCH_QUERY)
        },
        onQuickCategoryClick = { category ->
            viewModel.onSearchQueryChanged(category)
            viewModel.searchShows(category)
        },
        uiState = searchUiState,
        onShowClick = { show ->
            navController.navigate("detail/${show.id}")
        },
        onRetry = { viewModel.searchShows() }
    )
}

// STATELESS COMPOSABLE (Tampilan Murni Dark Emerald Glassmorphism)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessHomeScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearSearch: () -> Unit,
    onResetToDefault: () -> Unit,
    onQuickCategoryClick: (String) -> Unit,
    uiState: SearchUiState,
    onShowClick: (Show) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quickCategories = listOf("Marvel", "Action", "Drama", "Anime", "Comedy", "Sci-Fi", "Batman")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0x3310B981),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, EmeraldBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = "Logo",
                                tint = EmeraldPrimary,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CineExplore",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "TVmaze API Explorer",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldPrimary
                            )
                        }
                    }
                },
                actions = {
                    // Tombol Reset ke Beranda / Rekomendasi Awal
                    IconButton(onClick = onResetToDefault) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Reset Beranda",
                            tint = EmeraldPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBg,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkBg,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Kolom Pencarian Film Glassmorphic
            CineSearchBar(
                query = searchQuery,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                onClear = onClearSearch
            )

            // 2. Chips Kategori Cepat (Colorful & Mudah Diuji)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickCategories) { category ->
                    val isSelected = searchQuery.equals(category, ignoreCase = true)
                    Surface(
                        onClick = { onQuickCategoryClick(category) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) EmeraldPrimary else DarkSurfaceCard,
                        border = BorderStroke(1.dp, if (isSelected) EmeraldPrimary else EmeraldBorder)
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.Black else TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 3. Status Rendering berdasarkan StateFlow UI State
            when (uiState) {
                is SearchUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = EmeraldPrimary,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Menghubungkan ke TVmaze API...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }

                is SearchUiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.shows,
                            key = { it.id }
                        ) { show ->
                            ShowItemCard(
                                show = show,
                                onClick = { onShowClick(show) }
                            )
                        }
                    }
                }

                is SearchUiState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = BorderStroke(1.dp, EmeraldBorder),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = "Tidak Ditemukan",
                                    tint = AccentRose,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Film Tidak Ditemukan",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Coba gunakan kata kunci atau pilih kategori cepat di atas.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                is SearchUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = BorderStroke(1.dp, AccentRose.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Error",
                                    tint = AccentRose,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Gagal Memuat Data",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentRose
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = uiState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onRetry,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EmeraldPrimary,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Coba Lagi", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                is SearchUiState.Idle -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = "Idle",
                                tint = EmeraldPrimary.copy(alpha = 0.6f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Mulai Menjelajah",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Ketik judul film atau pilih kategori cepat di atas.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
