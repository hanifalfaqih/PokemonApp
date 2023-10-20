package id.allana.pokemonapp.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val imagesUrl: String
)