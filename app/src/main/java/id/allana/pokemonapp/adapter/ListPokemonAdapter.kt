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
import id.allana.pokemonapp.cache.PokemonEntity
import id.allana.pokemonapp.databinding.ItemPokemonLayoutBinding

class ListPokemonAdapter: PagingDataAdapter<PokemonEntity, ListPokemonAdapter.ViewHolder>(PokemonComparators()) {

    inner class ViewHolder(private val binding: ItemPokemonLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PokemonEntity) {
            binding.apply {
                tvPokemonName.text = data.name
                Glide.with(itemView.context)
                    .load(data.imagesUrl)
                    .placeholder(R.drawable.ic_placeholder_image)
                    .into(sivPokemonPoster)
            }
            itemView.setOnClickListener {
                val actionToDetail = ListPokemonFragmentDirections.actionListPokemonFragmentToDetailPokemonFragment(data.id)
                it.findNavController().navigate(actionToDetail)
            }
        }
    }

    class PokemonComparators: DiffUtil.ItemCallback<PokemonEntity>() {
        override fun areItemsTheSame(
            oldItem: PokemonEntity,
            newItem: PokemonEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PokemonEntity,
            newItem: PokemonEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: ListPokemonAdapter.ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListPokemonAdapter.ViewHolder {
        val binding = ItemPokemonLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}