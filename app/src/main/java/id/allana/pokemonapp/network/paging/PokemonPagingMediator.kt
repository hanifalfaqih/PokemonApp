package id.allana.pokemonapp.network.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.allana.pokemonapp.cache.PokemonDatabase
import id.allana.pokemonapp.cache.PokemonEntity
import id.allana.pokemonapp.cache.RemoteKeys
import id.allana.pokemonapp.model.util.Constant.INITIAL_PAGE_INDEX
import id.allana.pokemonapp.network.api.PokemonApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonPagingMediator(
    private val service: PokemonApiService,
    private val pokemonDatabase: PokemonDatabase
): RemoteMediator<Int, PokemonEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstTime(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastTime(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val apiResponse = service.getAllPokemon(page, state.config.pageSize)

            val pokemon = apiResponse.data
            val endOfPaginationReached = pokemon.isEmpty()
            pokemonDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDatabase.remoteKeysDao().clearRemoteKeys()
                    pokemonDatabase.pokemonsDao().clearPokemons()
                }
                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = pokemon.map {
                    RemoteKeys(pokemonId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                val pokemonEntity = pokemon.map {
                    PokemonEntity(id = it.id, name = it.name, imagesUrl = it.images.small)
                }
                pokemonDatabase.remoteKeysDao().insertAll(keys)
                pokemonDatabase.pokemonsDao().insertAll(pokemonEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastTime(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.pages.lastOrNull() {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { pokemon ->
            pokemonDatabase.remoteKeysDao().remoteKeysRepoId(pokemonId = pokemon.id)
        }
    }

    private suspend fun getRemoteKeyForFirstTime(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                pokemonDatabase.remoteKeysDao().remoteKeysRepoId(pokemonId = pokemon.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { pokemonId ->
                pokemonDatabase.remoteKeysDao().remoteKeysRepoId(pokemonId)
            }
        }
    }


}