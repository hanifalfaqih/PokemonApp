package id.allana.pokemonapp.model.response

import com.google.gson.annotations.SerializedName

data class PokemonDetail(

    @field:SerializedName("supertype")
    val supertype: String,

    @field:SerializedName("images")
    val images: ImagesPokemon,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

)
