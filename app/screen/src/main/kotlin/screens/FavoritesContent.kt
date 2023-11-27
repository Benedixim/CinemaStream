
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Snackbar
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
//import com.example.cinemastream.network.API_KEY
//import com.example.cinemastream.db.Movie
//import com.example.cinemastream.network.getPopularMovies
//import com.example.cinemastream.network.searchMovie
//import com.example.cinemastream.model.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.ApiStatus


@ApiStatus.Internal
@Composable
fun FavoritesContent(movies: SnapshotStateList<Movie>) {


    var showSnackbar by remember { mutableStateOf(false) }

    if (showSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            backgroundColor = Color.White // Устанавливаем белый цвет текста

        ) {
            Text(text = "Приветствуем вас!")
        }

    }
    // Content of Favorites page
    val films = remember { mutableStateListOf<Film>() }
    val searchQuery = remember { mutableStateOf("") }


    // Получение списка популярных фильмов внутри Composable-функции
    LaunchedEffect(Unit) {
        val popularMovies = withContext(Dispatchers.IO) {
            getPopularMovies(API_KEY)
        }
        if (popularMovies != null) {
            films.addAll(popularMovies)
        } else {
            // Обработка ситуации, когда popularMovies равно null
            println("Popular movies is null")
        }
    }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
        showSnackbar = true
    }

    LaunchedEffect(Unit) {
        delay(7000)

        showSnackbar = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Поисковое поле для ввода названия фильма
            TextField(
                value = searchQuery.value,
                onValueChange = { newValue ->
                    searchQuery.value = newValue
                },
                label = { Text("Enter movie name") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Button(
                onClick = {
                    if (searchQuery.value.isNotEmpty()) {
                        val searchResults = runBlocking {
                            withContext(Dispatchers.IO) {
                                searchMovie(API_KEY, searchQuery.value)
                            }
                        }
                        films.clear()
                        films.addAll(searchResults)
                    }
                },
                modifier = Modifier
                    .padding(end = 16.dp, top = 8.dp)
                    .background(Color.Transparent)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Text("\uD83D\uDD0E")
            }

            Button(
                onClick = {
                    films.clear()
                    val popularMovies = runBlocking {
                        withContext(Dispatchers.IO) {
                            getPopularMovies(API_KEY)
                        }
                    }
                    films.addAll(popularMovies)
                    searchQuery.value = ""
                },
                modifier = Modifier
                    .padding(end = 8.dp, top = 8.dp)
                    .background(Color.Transparent)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Text("\u2716")
            }
        }


        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(films) { index, film ->
                if (index % 2 == 0) {
                    Row(Modifier.fillMaxWidth()) {
                        MovieItem(film, movies)
                        if (index + 1 < films.size) {
                            MovieItem(films[index + 1], movies)
                        }
                    }
                }
            }
        }
    }
}
