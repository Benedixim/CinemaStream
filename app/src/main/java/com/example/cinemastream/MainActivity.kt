package com.example.cinemastream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemastream.db.Movie
import com.example.cinemastream.screens.BottomNavigationMenu
import com.example.cinemastream.ui.theme.CinemaStreamTheme
import kotlinx.coroutines.launch
import org.jetbrains.annotations.ApiStatus


@ApiStatus.Internal
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    var isLoading = mutableStateOf(false);
    var movies = mutableStateListOf<Movie>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaStreamTheme {
                if (isLoading.value) {
                    // Show a progress indicator while loading data
                    CircularProgressIndicator()
                } else {

                    // Show the loaded data
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        BottomNavigationMenu(movies);
                        //Greeting("Android")
                    }
                }
                // A surface container using the 'background' color from the theme

            }
        }
    }

    class MainViewModel : ViewModel() {
        private val repository = MovieRepository()

        val isLoading = mutableStateOf(false)
        val movies = mutableStateListOf<Movie>()

        init {
            loadMovies()
        }

        private fun loadMovies() {
            isLoading.value = true
            viewModelScope.launch {
                movies.addAll(repository.getMovies())
                isLoading.value = false
            }
        }
    }

    class MovieRepository {

        suspend fun getMovies(): List<Movie> {
            return mutableStateListOf<Movie>();
        }
    }
}


