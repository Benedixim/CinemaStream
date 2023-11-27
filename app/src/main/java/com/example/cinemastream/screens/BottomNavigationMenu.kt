package com.example.cinemastream.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.example.cinemastream.db.Movie
import org.jetbrains.annotations.ApiStatus


@ApiStatus.Internal
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