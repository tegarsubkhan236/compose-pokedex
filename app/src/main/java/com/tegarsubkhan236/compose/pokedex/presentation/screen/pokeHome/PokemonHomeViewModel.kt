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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonHomeViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel(){

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isloading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isloading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {

                }
                is Resource.Error -> {

                }
            }
        }
    }

    fun calcDominantColor(
        drawable: Drawable,
        onFinish: (Color) -> Unit
    ) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate{palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

}