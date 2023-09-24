package id.allana.pokemonapp.model.response

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @field:SerializedName("images")
    val images: ImagesPokemon,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,
    )