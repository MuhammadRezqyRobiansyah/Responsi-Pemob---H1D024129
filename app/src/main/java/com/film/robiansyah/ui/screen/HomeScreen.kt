package com.film.robiansyah.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
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
        uiState = searchUiState,
        onShowClick = { show ->
            navController.navigate("detail/${show.id}")
        },
        onRetry = { viewModel.searchShows() }
    )
}

// STATELESS COMPOSABLE (Tampilan Murni Neo-Brutalism Dark)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessHomeScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearSearch: () -> Unit,
    uiState: SearchUiState,
    onShowClick: (Show) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = "Logo",
                                tint = NeoLime,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CINE.EXPLORE",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    letterSpacing = 1.sp
                                ),
                                fontWeight = FontWeight.Black,
                                color = NeoTextWhite
                            )
                        }

                        // Badge Tag Khas Neo-Brutalism
                        Surface(
                            color = NeoLime,
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.5.dp, Color.Black)
                        ) {
                            Text(
                                text = "TVMAZE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NeoBackground,
                    titleContentColor = NeoTextWhite
                )
            )
        },
        containerColor = NeoBackground,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Kolom Pencarian Film
            CineSearchBar(
                query = searchQuery,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                onClear = onClearSearch
            )

            // Status Rendering berdasarkan StateFlow UI State
            when (uiState) {
                is SearchUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = NeoLime,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Mencari film di TVmaze...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NeoTextWhite,
                                fontWeight = FontWeight.SemiBold
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
                            colors = CardDefaults.cardColors(containerColor = NeoSurface),
                            border = BorderStroke(2.dp, NeoBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = "Tidak Ditemukan",
                                    tint = NeoCoral,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Film Tidak Ditemukan",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NeoTextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Coba gunakan kata kunci judul film lain.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NeoTextMuted,
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
                            colors = CardDefaults.cardColors(containerColor = NeoSurface),
                            border = BorderStroke(2.dp, NeoCoral),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Error",
                                    tint = NeoCoral,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Gagal Memuat Data",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NeoCoral
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = uiState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NeoTextWhite,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onRetry,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NeoLime,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(2.dp, Color.Black)
                                ) {
                                    Text(
                                        text = "Coba Lagi",
                                        fontWeight = FontWeight.Bold
                                    )
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
                                tint = NeoLime,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Mulai Pencarian",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NeoTextWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Ketik judul film atau serial TV di atas lalu tekan cari.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NeoTextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
