package id.allana.pokemonapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.allana.pokemonapp.model.response.Pokemon
import id.allana.pokemonapp.model.util.Constant.INITIAL_PAGE_INDEX
import id.allana.pokemonapp.model.util.Constant.PAGE_SIZE
import id.allana.pokemonapp.network.api.PokemonApiService

class PokemonPagingSource(private val apiService: PokemonApiService): PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllPokemon(page, PAGE_SIZE)

            LoadResult.Page(
                data = responseData.data,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (responseData.data.isNullOrEmpty()) null else page.plus(1)
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}