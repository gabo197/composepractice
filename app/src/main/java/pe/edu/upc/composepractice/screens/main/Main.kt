package pe.edu.upc.composepractice.screens.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.edu.upc.composepractice.screens.favorites.Favorites
import pe.edu.upc.composepractice.screens.search.Search
import pe.edu.upc.composepractice.screens.people.People

@Composable
fun Main(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "search") {
        composable("search") {
            Search {
                navController.navigate("heroItem/${it}")
            }
        }
        composable("people") {
            People()
        }
        composable("favorites") {
            Favorites()
        }
    }
}