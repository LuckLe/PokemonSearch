@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.rocketreserver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rocketreserver.apollo.PokemonDetails
import com.example.rocketreserver.apollo.PokemonList
import com.example.rocketreserver.ui.theme.RocketReserverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferencesUtils.init(this)
        setContent {
            RocketReserverTheme {
                Scaffold { paddingValues ->
                    Box(Modifier.padding(paddingValues)) {
                        MainNavHost()
                    }
                }
            }
        }
    }
}

@Composable
private fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationDestinations.POKEMON_LIST) {
        composable(route = NavigationDestinations.POKEMON_LIST) {
            PokemonList{
                navController.navigate("${NavigationDestinations.POKEMON_DETAILS}/${it.id}")
            }
        }

        composable(route = "${NavigationDestinations.POKEMON_DETAILS}/{${NavigationArguments.POKEMON_ID}}") { navBackStackEntry ->
            navBackStackEntry.arguments?.getString(NavigationArguments.POKEMON_ID)?.toInt()
                ?.let { PokemonDetails(it,navController) }
        }
    }
}
