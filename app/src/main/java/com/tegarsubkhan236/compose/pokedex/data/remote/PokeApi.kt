package com.tegarsubkhan236.compose.pokedex.data.remote

import com.tegarsubkhan236.compose.pokedex.data.remote.responses.Pokemon
import com.tegarsubkhan236.compose.pokedex.data.remote.responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ) : PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ) : Pokemon

}