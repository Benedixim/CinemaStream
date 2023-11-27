package com.example.cinemastream.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.cinemastream.db.Movie
import com.example.cinemastream.model.Film
import org.jetbrains.annotations.ApiStatus


@ApiStatus.Internal
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