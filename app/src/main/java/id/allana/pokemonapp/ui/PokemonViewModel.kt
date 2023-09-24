package id.allana.pokemonapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.allana.pokemonapp.model.response.Pokemon
import id.allana.pokemonapp.model.response.ResponseAllPokemon
import id.allana.pokemonapp.model.response.ResponseDetailPokemon
import id.allana.pokemonapp.network.api.ApiConfig
import id.allana.pokemonapp.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    private val pokemonApiService = ApiConfig.getApiService()
    private val pokemonRepository = PokemonRepository(pokemonApiService)

    val listPokemon: LiveData<PagingData<Pokemon>> =
        pokemonRepository.getAllPokemon().cachedIn(viewModelScope)

    private val _detailPokemon = MutableLiveData<ResponseDetailPokemon>()
    val detailPokemon: LiveData<ResponseDetailPokemon> = _detailPokemon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listSearchPokemon = MutableLiveData<ResponseAllPokemon>()
    val listSearchPokemon: LiveData<ResponseAllPokemon> = _listSearchPokemon

    fun searchPokemon(query: String) {
        val searchQuery = "name:$query"
        _isLoading.value = true
        viewModelScope.launch {
            _listSearchPokemon.value = pokemonRepository.searchPokemon(searchQuery)
            _isLoading.value = false
        }
    }

    fun getDetailPokemon(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _detailPokemon.value = pokemonRepository.getDetailPokemon(id)
            _isLoading.value = false
        }
    }
}