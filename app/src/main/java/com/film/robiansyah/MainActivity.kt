package com.film.robiansyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.film.robiansyah.ui.screen.DetailScreen
import com.film.robiansyah.ui.screen.HomeScreen
import com.film.robiansyah.ui.theme.CineExploreTheme
import com.film.robiansyah.ui.viewmodel.TvShowViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineExploreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CineExploreApp()
                }
            }
        }
    }
}

@Composable
fun CineExploreApp() {
    val navController = rememberNavController()
    // TvShowViewModel terpusat yang dibagikan antar rute navigasi
    val tvShowViewModel: TvShowViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Rute 1: Home Screen (Pencarian & Daftar Film)
        composable(route = "home") {
            HomeScreen(
                navController = navController,
                viewModel = tvShowViewModel
            )
        }

        // Rute 2: Detail Screen (Informasi Lengkap Film berdasarkan showId)
        composable(
            route = "detail/{showId}",
            arguments = listOf(
                navArgument(name = "showId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val showId = backStackEntry.arguments?.getInt("showId") ?: 0
            DetailScreen(
                showId = showId,
                navController = navController,
                viewModel = tvShowViewModel
            )
        }
    }
}
