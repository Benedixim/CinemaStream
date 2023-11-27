package com.example.cinemastream.mvi

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.rememberImagePainter
import com.example.cinemastream.db.Movie
import com.example.cinemastream.model.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.annotations.ApiStatus


//MVI
@ApiStatus.Internal
data class MovieItemViewState(
    val showDialog: Boolean = false
)
@ApiStatus.Internal
sealed class MovieItemAction {
    object ToggleDialog : MovieItemAction()
    object AddToFavorites : MovieItemAction()
}
@ApiStatus.Internal
@Composable
fun MovieItem2(movie: Film, movies: SnapshotStateList<Movie>) {
    val viewModel = remember { MovieItemViewModel() }
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .clickable {
                viewModel.processAction(MovieItemAction.ToggleDialog)
            }
            .padding(8.dp)
            .width(150.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
        ) {
            Image(
                painter = rememberImagePainter(movie.posterUrlPreview),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = movie.nameRu,
            modifier = Modifier.padding(vertical = 4.dp),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = movie.year,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color.White
        )
        if (viewState.showDialog) {
            // Отображение AlertDialog при showDialog == true
            AlertDialog(
                onDismissRequest = {
                    viewModel.processAction(MovieItemAction.ToggleDialog)
                },
                title = { Text(text = "Подтверждение действия") },
                text = { Text("Вы действительно хотите добавить фильм в избранное?") },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val durationString = movie.filmLength
                                val parts = durationString.split(":")
                                val hours = parts[0].toInt()
                                val minutes = parts[1].toInt()
                                val totalMinutes = hours * 60 + minutes

                                val newMovie = Movie(
                                    5,
                                    movie.nameRu,
                                    movie.year.toInt(),
                                    movie.genres.toString(),
                                    totalMinutes,
                                    movie.posterUrlPreview
                                )
                                movies.add(newMovie)

                                viewModel.processAction(MovieItemAction.ToggleDialog)
                                viewModel.processAction(MovieItemAction.AddToFavorites)
                            }
                        ) {
                            Text("Добавить в избранное")
                        }
                    }
                }
            )
        }
    }
}
@ApiStatus.Internal
class MovieItemViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(MovieItemViewState())

    val viewState: StateFlow<MovieItemViewState> = _viewState.asStateFlow()

    fun processAction(action: MovieItemAction) {
        when (action) {
            is MovieItemAction.ToggleDialog -> {
                _viewState.value = _viewState.value.copy(showDialog = !_viewState.value.showDialog)
            }
            is MovieItemAction.AddToFavorites -> {
            }
        }
    }
}

//MVI