@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.rocketreserver

import android.content.Context
import android.os.Bundle
import android.util.Log
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
        TokenRepository.init(this)
        setContent {
            RocketReserverTheme {
                Scaffold { paddingValues ->
                    Box(Modifier.padding(paddingValues)) {
                        MainNavHost(this@MainActivity)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainNavHost(context: Context) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationDestinations.LAUNCH_LIST) {
//        composable(route = NavigationDestinations.LAUNCH_LIST) {
//            LaunchList(
//                onLaunchClick = { launchId ->
//                    navController.navigate("${NavigationDestinations.LAUNCH_DETAILS}/$launchId")
//                }
//            )
//        }
        composable(route = NavigationDestinations.LAUNCH_LIST) {
            PokemonList{
                Log.i("hml","点击了：${it.name}")
                navController.navigate("${NavigationDestinations.LAUNCH_DETAILS}/${it.id}")
            }
        }

        composable(route = "${NavigationDestinations.LAUNCH_DETAILS}/{${NavigationArguments.POKEMON_ID}}") { navBackStackEntry ->
//            LaunchDetails(launchId = navBackStackEntry.arguments!!.getString(NavigationArguments.LAUNCH_ID)!!)
            PokemonDetails(navBackStackEntry.arguments!!.getString(NavigationArguments.POKEMON_ID)!!.toInt(),navController)
        }

        composable(route = NavigationDestinations.LOGIN) {
            Login()
        }
    }
}
