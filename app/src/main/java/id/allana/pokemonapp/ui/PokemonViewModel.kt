package id.allana.pokemonapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.allana.pokemonapp.cache.PokemonDatabase
import id.allana.pokemonapp.cache.PokemonEntity
import id.allana.pokemonapp.model.response.Pokemon
import id.allana.pokemonapp.model.response.ResponseDetailPokemon
import id.allana.pokemonapp.network.api.ApiConfig
import id.allana.pokemonapp.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

    private val pokemonApiService = ApiConfig.getApiService()
    private val pokemonDatabase = PokemonDatabase.getInstance(application)
    private val pokemonRepository = PokemonRepository(pokemonApiService, pokemonDatabase)

    val listPokemon: LiveData<PagingData<PokemonEntity>> =
        pokemonRepository.getAllPokemon().cachedIn(viewModelScope)

    private val _detailPokemon = MutableLiveData<ResponseDetailPokemon>()
    val detailPokemon: LiveData<ResponseDetailPokemon> = _detailPokemon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    fun searchPokemon(query: String): LiveData<PagingData<Pokemon>> {
        val searchQuery = "name:$query"
        return pokemonRepository.searchPokemon(searchQuery).cachedIn(viewModelScope)
    }

    fun getDetailPokemon(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _detailPokemon.value = pokemonRepository.getDetailPokemon(id)
            _isLoading.value = false
        }
    }
}