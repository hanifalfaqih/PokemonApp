package id.allana.pokemonapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import id.allana.pokemonapp.cache.PokemonDatabase
import id.allana.pokemonapp.cache.PokemonEntity
import id.allana.pokemonapp.model.response.Pokemon
import id.allana.pokemonapp.model.response.ResponseDetailPokemon
import id.allana.pokemonapp.network.api.PokemonApiService
import id.allana.pokemonapp.network.paging.PokemonPagingMediator
import id.allana.pokemonapp.network.paging.PokemonPagingSource

class PokemonRepository(
    private val pokemonApiService: PokemonApiService,
    private val pokemonDatabase: PokemonDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllPokemon(): LiveData<PagingData<PokemonEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = PokemonPagingMediator(pokemonApiService, pokemonDatabase),
            pagingSourceFactory = {
                pokemonDatabase.pokemonsDao().getPokemons()
            }
        ).liveData
    }

    fun searchPokemon(query: String): LiveData<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                PokemonPagingSource(query, pokemonApiService)
            }
        ).liveData
    }

    suspend fun getDetailPokemon(id: String): ResponseDetailPokemon {
        return pokemonApiService.getDetailPokemon(id)
    }

}