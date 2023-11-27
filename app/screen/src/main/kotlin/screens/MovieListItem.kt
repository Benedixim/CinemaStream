
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
//import com.example.cinemastream.db.Movie
import org.jetbrains.annotations.ApiStatus


@ApiStatus.Internal
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