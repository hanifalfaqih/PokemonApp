package id.allana.pokemonapp.model.response

import com.google.gson.annotations.SerializedName

data class ResponseDetailPokemon(

	@field:SerializedName("data")
	val pokemonDetail: PokemonDetail
)


