package com.tegarsubkhan236.compose.pokedex.data.repository

import com.tegarsubkhan236.compose.pokedex.data.remote.PokeApi
import com.tegarsubkhan236.compose.pokedex.data.remote.responses.Pokemon
import com.tegarsubkhan236.compose.pokedex.data.remote.responses.PokemonList
import com.tegarsubkhan236.compose.pokedex.data.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {
    suspend fun getPokemonList(limit: Int, offset: Int) : Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonDetail(pokemonName: String) : Resource<Pokemon> {
        val response = try {
            api.getPokemonDetail(pokemonName)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }
}