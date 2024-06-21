package com.tegarsubkhan236.compose.pokedex.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tegarsubkhan236.compose.pokedex.presentation.screen.pokeHome.PokeHomeScreen

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "poke_home"
    ) {
        composable(route = "poke_home"){
            PokeHomeScreen()
        }
    }
}