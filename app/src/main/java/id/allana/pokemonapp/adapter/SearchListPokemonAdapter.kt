package id.allana.pokemonapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.allana.pokemonapp.ListPokemonFragmentDirections
import id.allana.pokemonapp.databinding.ItemPokemonLayoutBinding
import id.allana.pokemonapp.model.response.Pokemon

class SearchListPokemonAdapter: ListAdapter<Pokemon, SearchListPokemonAdapter.ViewHolder>(ListPokemonAdapter.PokemonComparators()) {

    inner class ViewHolder(private val binding: ItemPokemonLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Pokemon) {
            binding.apply {
                tvPokemonName.text = data.name
                Glide.with(itemView.context)
                    .load(data.images.small)
                    .into(sivPokemonPoster)
            }
            itemView.setOnClickListener {
                val actionToDetail = ListPokemonFragmentDirections.actionListPokemonFragmentToDetailPokemonFragment(data.id)
                it.findNavController().navigate(actionToDetail)
            }
        }
    }

    override fun onBindViewHolder(holder: SearchListPokemonAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListPokemonAdapter.ViewHolder {
        val binding = ItemPokemonLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}