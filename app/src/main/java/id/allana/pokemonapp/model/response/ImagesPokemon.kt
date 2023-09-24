package id.allana.pokemonapp.model.response

import com.google.gson.annotations.SerializedName

data class ImagesPokemon(
    @field:SerializedName("symbol")
    val symbol: String,

    @field:SerializedName("logo")
    val logo: String,

    @field:SerializedName("small")
    val small: String,

    @field:SerializedName("large")
    val large: String
)
