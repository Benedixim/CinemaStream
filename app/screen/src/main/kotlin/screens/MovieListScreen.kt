
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.room.Room
//import com.example.cinemastream.db.AppDatabase
//import com.example.cinemastream.db.Movie
//import com.example.cinemastream.db.printMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.ApiStatus


@ApiStatus.Internal
@Composable
fun MovieListScreen(movies: SnapshotStateList<Movie>) {

    val applicationContext = LocalContext.current.applicationContext
    val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my-database")
        .build()
    val movieDao = db.movieDao()

    // Получаем список фильмов из базы данных
    val moviesFromDatabase = movieDao.getAllMovies()


//    val moviesToAdd = listOf(
//        Movie(1, "The Shawshank Redemption", 1994, "Drama", 142, "https://i.pinimg.com/736x/bf/50/8b/bf508ba67a95df1148bd3bbf936d06b7--greatest-movies-good-movies.jpg"),
//        Movie(2, "The Godfather", 1972, "Crime, Drama", 175, "https://sothebys-md.brightspotcdn.com/f8/a5/5c3aea3243d1b8877386b87e23fd/19.%20THE%20GODFATHER%20AUSTRALIAN.jpg"),
//        // Добавьте остальные фильмы
//    )
//
//    movieDao.insertMovies(moviesToAdd)


//    LaunchedEffect(Unit) {
//
//        for (movie in movies {
//            withContext(Dispatchers.IO) {
//                movieDao.insert(movie)
//            }
//        }
//    }

    // Читаем поток и преобразуем его в список
    val moviesList = remember { mutableStateListOf<Movie>() }



    LaunchedEffect(Unit) {
        val movies = withContext(Dispatchers.IO) {
            movieDao.getAllMovies().first() // Получаем первый элемент из потока
        }
        moviesList.addAll(movies)
        printMovies(moviesList) // Вывод фильмов на консоль
    }










//        Movie("The Shawshank Redemption", 1994, "Drama", 142, "https://i.pinimg.com/736x/bf/50/8b/bf508ba67a95df1148bd3bbf936d06b7--greatest-movies-good-movies.jpg"),
//        Movie("The Godfather", 1972, "Crime, Drama", 175, "https://sothebys-md.brightspotcdn.com/f8/a5/5c3aea3243d1b8877386b87e23fd/19.%20THE%20GODFATHER%20AUSTRALIAN.jpg"),
//        Movie("The Dark Knight", 2008, "Action, Crime, Drama", 152, "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_FMjpg_UY2048_.jpg"),
//        Movie("12 Angry Men", 1957, "Drama", 96, "https://m.media-amazon.com/images/M/MV5BNDhjMjE4NDItZTkyOC00NjIwLWI0MDQtYTJhZjY2YzlkMDQ0XkEyXkFqcGdeQXVyMTA0MTM5NjI2._V1_.jpg"),
//        Movie("Schindler's List", 1993, "Biography, Drama, History", 195, "https://m.media-amazon.com/images/I/81+H4lZVw+L._AC_UF894,1000_QL80_.jpg"),


    // загрузка списка фильмов при первом запуске экрана

    val createDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth() ) {
        // кнопки для CRUD функций



        Box(modifier = Modifier.fillMaxWidth())
        {
            // список фильмов
            LazyColumn(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth()

            )
            {

                items(moviesList) { movie ->
                    MovieListItem(movie, movies)
                }
                items(movies) { movie ->
                    MovieListItem(movie, movies)
                }

            }

            Box(
                contentAlignment = Alignment.Center, // выравнивание контента по центру
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 650.dp)
            ){
                Button(
                    onClick = { createDialog.value = true;  },
                    shape = CircleShape, // задаем форму кнопки как круглую
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.Black),
                    modifier = Modifier
                        .zIndex(1f) // устанавливаем положение кнопки поверх остальных элементов
                        .padding(6.dp) // добавляем отступы к кнопке для лучшего отображения

                ) {
                    Text("+")
                }
            }
        }




    }

    if (createDialog.value) {
        var title by remember { mutableStateOf("") }
        var releaseYear by remember { mutableStateOf("") }
        var genre by remember { mutableStateOf("") }
        var duration by remember { mutableStateOf("") }
        var posterUrl by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                createDialog.value = false
            },
            title = { Text("Добавить фильм") },
            text = {
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Название") }
                    )
                    TextField(
                        value = releaseYear,
                        onValueChange = { releaseYear = it },
                        label = { Text("Год выпуска") }
                    )
                    TextField(
                        value = genre,
                        onValueChange = { genre = it },
                        label = { Text("Жанр") }
                    )
                    TextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Продолжительность (в минутах)") }
                    )
                    TextField(
                        value = posterUrl,
                        onValueChange = { posterUrl = it },
                        label = { Text("URL постера") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newMovie = Movie(5, title, releaseYear.toInt(), genre, duration.toInt(), posterUrl)
                        movies.add(newMovie)
                        createDialog.value = false
                    }
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        createDialog.value = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

}
