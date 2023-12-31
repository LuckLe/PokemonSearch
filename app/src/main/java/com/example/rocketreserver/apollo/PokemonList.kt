package com.example.rocketreserver.apollo

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rocketreserver.R
import com.example.rocketreserver.SpeciesQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

val pkSpeciesDataFlow : MutableStateFlow<List<SpeciesQuery.Pokemon_v2_pokemonspecy>> = MutableStateFlow(
    emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonList(onItemClick: (pokemon: SpeciesQuery.Pokemon_v2_pokemon) -> Unit) {
    val context2 = LocalContext.current
    val pkSpeciesData : List<SpeciesQuery.Pokemon_v2_pokemonspecy> = pkSpeciesDataFlow.collectAsState().value
    val scope = rememberCoroutineScope()
    var loading by remember {
        mutableStateOf(false)
    }
    var value by remember { mutableStateOf("")}

    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
        )
    )

    fun startLoading(){
        pkSpeciesDataFlow.value = emptyList()
        loading = true
        scope.launch {
            try {
                val response = apolloClient.query(SpeciesQuery("%$value%")).execute()
                val data = response.data?.pokemon_v2_pokemonspecies ?: emptyList()
                pkSpeciesDataFlow.value = data
                if (data.isEmpty()){
                    Toast.makeText(context2, "not find species, please enter the other species name" , Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Toast.makeText(context2, "request failed, please enter the correct species name" , Toast.LENGTH_SHORT).show()
            }
            loading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                stringResource(R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )
            Row {
                TextField(
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f),
                    value = value,
                    onValueChange = {
                        value = it
                    },
                    placeholder = {Text(text = "input species name")},
                )

                Button(modifier = Modifier.height(60.dp), onClick = {
                    Log.i("hml","点击搜索：$value")
                    startLoading()
                }, enabled = value.isNotEmpty()) {
                    Text(text = "Search")
                }
            }


            AnimatedVisibility(visible = pkSpeciesData.isNotEmpty()) {
                LazyColumn(modifier = Modifier.background(Color(0xFFEEEEEE))) {
                    items(pkSpeciesData) {
                        SpeciesItem(it, onItemClick = onItemClick)
                    }
                }
            }
        }

        AnimatedVisibility(visible = loading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0x50000000)),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier.graphicsLayer { rotationZ = angle },
                    imageVector = Icons.Filled.Refresh, contentDescription =""
                )
            }
        }
    }

}

@Composable
fun SpeciesItem(speciesItem: SpeciesQuery.Pokemon_v2_pokemonspecy, onItemClick: (pokemon: SpeciesQuery.Pokemon_v2_pokemon) -> Unit) {
    Column(modifier = Modifier
        .padding(12.dp)
        .background(getBackgroundColor(speciesItem.pokemon_v2_pokemoncolor?.name))) {
        Text(text = "Species Name：${speciesItem.name}", modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
        Text(text = "Capture_rate：${speciesItem.capture_rate}", modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
        Text(text = "Pokemons as follows ：", modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
        Divider()
        speciesItem.pokemon_v2_pokemons.forEachIndexed { index, pokemonV2Pokemon ->
            PokemonItem(showDivider = index!=speciesItem.pokemon_v2_pokemons.size-1, item = pokemonV2Pokemon, onItemClick = onItemClick)
        }
    }
    Divider(modifier = Modifier.padding(bottom = 10.dp))
}
@Composable
fun PokemonItem(showDivider : Boolean, item: SpeciesQuery.Pokemon_v2_pokemon, onItemClick: (pokemon: SpeciesQuery.Pokemon_v2_pokemon) -> Unit) {
    Text(text = "Pokemon Name：${item.name}", modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp, bottom = 10.dp)
        .clickable { onItemClick(item) })
    if (showDivider){
        Divider()
    }
}

fun getBackgroundColor(name:String?):Color{
    return when(name){
        "black" -> Color.Black
        "blue" -> Color.Blue
        "brown" -> Color(0xFFA52A2A)
        "gray" -> Color.Gray
        "green" -> Color.Green
        "pink" -> Color(0xFFFFC0CB)
        "purple" -> Color(0XFF660099)
        "red" -> Color.Red
        "white" -> Color.White
        "yellow" -> Color.Yellow
        else -> Color.White
    }
}