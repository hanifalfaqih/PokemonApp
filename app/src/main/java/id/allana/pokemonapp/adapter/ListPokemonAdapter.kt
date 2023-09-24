package id.allana.pokemonapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.allana.pokemonapp.ListPokemonFragmentDirections
import id.allana.pokemonapp.R
import id.allana.pokemonapp.databinding.ItemPokemonLayoutBinding
import id.allana.pokemonapp.model.response.Pokemon

class ListPokemonAdapter: PagingDataAdapter<Pokemon, ListPokemonAdapter.ViewHolder>(PokemonComparators()) {

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

    class PokemonComparators: DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(
            oldItem: Pokemon,
            newItem: Pokemon
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Pokemon,
            newItem: Pokemon
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onBindViewHolder(holder: ListPokemonAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListPokemonAdapter.ViewHolder {
        val binding = ItemPokemonLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}