package com.example.rocketreserver.apollo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rocketreserver.PokemonQuery
import com.example.rocketreserver.R
import kotlinx.coroutines.flow.MutableStateFlow

val pokemonFlow : MutableStateFlow<PokemonQuery.Pokemon_v2_pokemon_by_pk?> = MutableStateFlow(null)

@Composable
fun PokemonDetails(pkId: Int, navController: NavHostController) {

    val pokemon : PokemonQuery.Pokemon_v2_pokemon_by_pk? = pokemonFlow.collectAsState().value

    LaunchedEffect(Unit) {
        pokemonFlow.value = null
        val response = apolloClient.query(PokemonQuery(pkId)).execute()
        pokemonFlow.value = response.data?.pokemon_v2_pokemon_by_pk
        Log.d("hml", "PokemonDetails Success ${response.data}")
    }

    Column(modifier = Modifier.padding(12.dp)) {
        Text(
            stringResource(R.string.pokemon_details),
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            textAlign = TextAlign.Center
        )
        Text(text = "Pokemon Name：${pokemon?.name}", modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
        Text(text = "Pokemon abilities as follows ：", modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
        val abilities = pokemon?.pokemon_v2_pokemonabilities ?: emptyList()
        LazyColumn {
            items(abilities) {
                PokemonAbilityItem(item = it)
            }
        }

        Button(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth(),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text(text = "go back")
        }
    }
}

@Composable
fun PokemonAbilityItem(item: PokemonQuery.Pokemon_v2_pokemonability) {
    Divider()
    Text(text = "Ability Name：${item.pokemon_v2_ability?.name}", modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp))
}