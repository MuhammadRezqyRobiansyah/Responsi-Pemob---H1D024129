package com.film.robiansyah.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.film.robiansyah.data.model.Show
import com.film.robiansyah.ui.theme.*
import com.film.robiansyah.ui.viewmodel.DetailUiState
import com.film.robiansyah.ui.viewmodel.TvShowViewModel

// STATEFUL COMPOSABLE (Menangani pengambilan data detail & navigasi kembali)
@Composable
fun DetailScreen(
    showId: Int,
    navController: NavController,
    viewModel: TvShowViewModel = viewModel()
) {
    LaunchedEffect(showId) {
        viewModel.loadShowDetail(showId)
    }

    val detailUiState by viewModel.detailUiState.collectAsState()

    StatelessDetailScreen(
        uiState = detailUiState,
        onBackClick = { navController.popBackStack() },
        onRetry = { viewModel.loadShowDetail(showId) }
    )
}

// STATELESS COMPOSABLE (Tampilan Murni Neo-Brutalism Dark)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StatelessDetailScreen(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "DETAIL FILM",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = NeoLime
                        )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is DetailUiState.Loading -> {
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
                                text = "Memuat detail film...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NeoTextWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                is DetailUiState.Error -> {
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
                                    text = "Gagal Memuat Detail",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NeoCoral
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = uiState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NeoTextWhite
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
                                    Text("Coba Lagi", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                is DetailUiState.Success -> {
                    val show = uiState.show
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        // Poster Besar Film dengan Border Tegas 2.dp khas Neo-Brutalism
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(2.dp, NeoBorder, RoundedCornerShape(8.dp))
                                .background(Color(0xFF141414)),
                            contentAlignment = Alignment.Center
                        ) {
                            val imageUrl = show.image?.original ?: show.image?.medium

                            if (!imageUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = show.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = null,
                                    tint = NeoLime,
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 1. Judul Film
                        Text(
                            text = show.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = NeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 2. Baris Badges Neo-Brutalism: Tahun Rilis, Rating, Status
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Badge Tahun Rilis (Warna Coral)
                            Surface(
                                color = NeoCoral,
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.5.dp, Color.Black)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Tahun",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = show.releaseYear,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }

                            // Badge Rating (Warna Lime)
                            Surface(
                                color = NeoLime,
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.5.dp, Color.Black)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${show.ratingText} / 10",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
                                    )
                                }
                            }

                            // Badge Status Tayang (Warna Cyan)
                            if (!show.status.isNullOrBlank()) {
                                Surface(
                                    color = NeoCyan,
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.5.dp, Color.Black)
                                ) {
                                    Text(
                                        text = show.status,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 3. Daftar Genre (Chips Neo-Brutalism)
                        if (!show.genres.isNullOrEmpty()) {
                            Text(
                                text = "GENRE",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = NeoLime,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                show.genres.forEach { genre ->
                                    Surface(
                                        color = NeoSurfaceVariant,
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.5.dp, NeoBorder)
                                    ) {
                                        Text(
                                            text = genre,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = NeoTextWhite,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        HorizontalDivider(thickness = 2.dp, color = NeoBorder)

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4. Ringkasan atau Deskripsi Film (Summary) di dalam Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NeoSurface),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(2.dp, NeoBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "SINOPSIS",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = NeoLime,
                                    letterSpacing = 1.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = show.cleanSummary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NeoTextWhite,
                                    lineHeight = 22.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
