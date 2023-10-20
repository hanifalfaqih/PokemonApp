package id.allana.pokemonapp.network.api

import id.allana.pokemonapp.model.response.ResponseAllPokemon
import id.allana.pokemonapp.model.response.ResponseDetailPokemon
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("cards?")
    suspend fun getAllPokemon(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ResponseAllPokemon

    @GET("cards/{id}")
    suspend fun getDetailPokemon(
        @Path("id") id: String
    ): ResponseDetailPokemon

    @GET("cards?")
    suspend fun searchPokemon(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ResponseAllPokemon
}