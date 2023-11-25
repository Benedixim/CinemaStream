package com.example.cinemastream

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Snackbar
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import coil.compose.rememberImagePainter

import com.example.cinemastream.ui.theme.CinemaStreamTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import androidx.lifecycle.viewModelScope
import com.example.cinemastream.Network.getPopularMovies
import com.example.cinemastream.Network.searchMovie
import com.example.cinemastream.model.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList


import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.format.TextStyle



class MainActivity : ComponentActivity() {
    public val apiKey = "8c8e1a50-6322-4135-8875-5d40a5420d86"
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


@Composable
fun MainActivityScreen() {
    val applicationContext = LocalContext.current.applicationContext
    val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my-database")
        .build()
    val movieDao = db.movieDao()

    // Получаем список фильмов из базы данных
    val moviesFromDatabase = movieDao.getAllMovies()

    // Ваш код Composable UI здесь
}

enum class Page {
    HOME, FAVORITES, PROFILE
}

@Composable
fun HomeContent() {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Text(
            text = "Home",
            modifier = Modifier.padding(16.dp)
        )
    }
}

const val API_KEY = "8c8e1a50-6322-4135-8875-5d40a5420d86"
const val API_URL_POPULAR =
    "https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS&page=1"




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


@Composable
fun MovieItem(movie: Film, movies: SnapshotStateList<Movie>) {
    val dialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .clickable {
                // Обработка клика на Column
                dialog.value = true
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
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = movie.year,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color.White
        )
        if (dialog.value) {
            // Отображение AlertDialog при dialog.value == true
            AlertDialog(
                onDismissRequest = {
                    dialog.value = false
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

                                dialog.value = false
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

//MVI

data class MovieItemViewState(
    val showDialog: Boolean = false
)

sealed class MovieItemAction {
    object ToggleDialog : MovieItemAction()
    object AddToFavorites : MovieItemAction()
}

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
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = movie.year,
            style = androidx.compose.ui.text.TextStyle(
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

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val releaseYear: Int,
    val genre: String,
    val duration: Int,
    val posterUrl: String
)

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)
}

@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}






@Composable
fun ProfileContent() {
    // Content of Profile page
    Text(
        text = "Profile",
        modifier = Modifier.padding(16.dp)
    )
}

//@Preview(showBackground = true)
@Composable
fun BottomNavigationMenu(movies: SnapshotStateList<Movie>) {
    val selectedPage = remember { mutableStateOf(Page.HOME) }

    Surface(
        color = Color(0xFFC569A0)
    ) {
        Column {

            // Content of each page




            // Bottom navigation menu
            BottomNavigation (
                modifier = Modifier.zIndex(2f)  ) {
                BottomNavigationItem(
                    selected = selectedPage.value == Page.HOME,
                    onClick = { selectedPage.value = Page.HOME },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )

                BottomNavigationItem(
                    selected = selectedPage.value == Page.FAVORITES,
                    onClick = { selectedPage.value = Page.FAVORITES },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") }
                )

                BottomNavigationItem(
                    selected = selectedPage.value == Page.PROFILE,
                    onClick = { selectedPage.value = Page.PROFILE },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }

            when (selectedPage.value) {
                Page.HOME -> FavoritesContent(movies)
                Page.FAVORITES -> MovieListScreen(movies)
                Page.PROFILE -> about()
            }
        }
    }
}




@Composable
fun about() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Color(0xFF542962))
            .padding(top = 70.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 139.dp, height = 141.dp)
                        .clip(shape = CircleShape)

                )

                Text(
                    text = "Андрей Колесинский\nJava Developer",
                    color = Color(0xFFF1A4CA),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 0.02.sp,
                    lineHeight = 26.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 25.dp, end = 16.dp, bottom = 8.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(4.dp),
                            clip = true
                        )

                        .clip(shape = RoundedCornerShape(4.dp))

                )}
            val context = LocalContext.current




            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.faq),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "FAQ",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Need more help?",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Github",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Fork the project on Github",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.linkedin),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Linkedin",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Contact us on Linkedin",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vk),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "VKontacte",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Visit our VKontakte page",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Email",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "benediximus21@mail.ru",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp, bottom = 80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.question),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Any other questions?",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Ask them here",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }



            // Add your code for the list here

        }}
}









var count=0;

suspend fun read(moviesFromDatabase: Flow<List<Movie>>): MutableList<Movie> {
    val moviesList: MutableList<Movie> = mutableListOf()
    moviesFromDatabase.collect { movies ->
        moviesList.addAll(movies)
    }
    return moviesList;
}


suspend fun printMovies(movies: List<Movie>) {
    for (movie in movies) {
        println(movie) // Вывод фильма на консоль
    }
}

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





@Composable
fun MovieListItem(movie: Movie, movies: SnapshotStateList<Movie>) {
    // отображение информации о фильме
    Row(
        //modifier = Modifier.clickable { /* Обработчик нажатия на элемент списка фильмов */ },
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberImagePainter(
                data = movie.posterUrl,
                builder = {
                    crossfade(true) // Включить плавный переход при загрузке изображения
                }
            ),
            contentDescription = movie.title,
            modifier = Modifier.size(80.dp)
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
                .width(170.dp)
        ) {
            Text(
                text = movie.title,

            )

            Text(
                text = movie.genre,

            )

            Text(
                text = "${movie.releaseYear}, ${movie.duration} min",
            )
        }

        val deleteDialog = remember { mutableStateOf(false) }
        val editDialog = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Button(
                modifier = Modifier.width(90.dp),
                onClick = { editDialog.value = true; }
                   ) {
                Text("Update")
            }
            Button(
                modifier = Modifier.width(90.dp),
                onClick = { deleteDialog.value = true;  }) {
                Text("Delete")
            }
        }
        if (deleteDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    deleteDialog.value = false
                },
                title = { Text(text = "Подтверждение действия") },
                text = { Text("Вы действительно хотите удалить выбранный элемент?") },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { deleteDialog.value = false; movies.remove(movie);
                                }
                        ) {
                            Text("Удалить")
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { deleteDialog.value = false; }
                        ) {
                            Text("Отмена")
                        }
                    }
                }
            )
        }

        if (editDialog.value) {
            var title by remember { mutableStateOf(movie.title) }
            var releaseYear by remember { mutableStateOf(movie.releaseYear.toString()) }
            var genre by remember { mutableStateOf(movie.genre) }
            var duration by remember { mutableStateOf(movie.duration.toString()) }
            var posterUrl by remember { mutableStateOf(movie.posterUrl) }

            AlertDialog(
                onDismissRequest = {
                    editDialog.value = false
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
                            val newMovie = Movie(5,title, releaseYear.toInt(), genre, duration.toInt(), posterUrl)
                            movies.set(movies.indexOf(movie), newMovie);
                            editDialog.value = false
                        }
                    ) {
                        Text("Изменить")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            editDialog.value = false
                        }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}



