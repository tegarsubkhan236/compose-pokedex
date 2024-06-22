package com.tegarsubkhan236.compose.pokedex.presentation.screen.pokeHome

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.tegarsubkhan236.compose.pokedex.data.model.PokedexListEntry
import com.tegarsubkhan236.compose.pokedex.data.repository.PokemonRepository
import com.tegarsubkhan236.compose.pokedex.data.util.Constants.PAGE_SIZE
import com.tegarsubkhan236.compose.pokedex.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonHomeViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isloading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchingStarting = true
    val isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isloading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(
                            entry.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            url,
                            number.toInt()
                        )
                    }
                    curPage++

                    loadError.value = ""
                    isloading.value = false
                    pokemonList.value += pokedexEntries
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isloading.value = false
                }
            }
        }
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchingStarting) {
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchingStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchingStarting) {
                cachedPokemonList = pokemonList.value
                isSearchingStarting = false
            }
            pokemonList.value = result
            isSearching.value = true
        }
    }

    fun calcDominantColor(
        drawable: Drawable,
        onFinish: (Color) -> Unit
    ) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

}