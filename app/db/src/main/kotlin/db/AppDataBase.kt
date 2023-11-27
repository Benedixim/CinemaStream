import androidx.room.Database
import androidx.room.RoomDatabase
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}