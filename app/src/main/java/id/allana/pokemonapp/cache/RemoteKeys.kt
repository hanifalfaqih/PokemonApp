package id.allana.pokemonapp.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val pokemonId: String,
    val prevKey: Int?,
    val nextKey: Int?
)