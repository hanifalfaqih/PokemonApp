package id.allana.pokemonapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import id.allana.pokemonapp.model.response.Pokemon
import id.allana.pokemonapp.model.response.ResponseAllPokemon
import id.allana.pokemonapp.model.response.ResponseDetailPokemon
import id.allana.pokemonapp.network.api.PokemonApiService
import id.allana.pokemonapp.network.paging.PokemonPagingSource

class PokemonRepository(
    private val pokemonApiService: PokemonApiService,
) {

    fun getAllPokemon(): LiveData<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                PokemonPagingSource(pokemonApiService)
            }
        ).liveData
    }

    suspend fun searchPokemon(query: String): ResponseAllPokemon {
        return pokemonApiService.searchPokemon(query)
    }

    suspend fun getDetailPokemon(id: String): ResponseDetailPokemon {
        return pokemonApiService.getDetailPokemon(id)
    }

}