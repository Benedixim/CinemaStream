import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val releaseYear: Int,
    val genre: String,
    val duration: Int,
    val posterUrl: String
)

@ApiStatus.Internal
suspend fun printMovies(movies: List<Movie>) {
    for (movie in movies) {
        println(movie) // Вывод фильма на консоль
    }
}