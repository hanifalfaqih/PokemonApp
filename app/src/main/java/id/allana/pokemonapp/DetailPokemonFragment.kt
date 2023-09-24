package id.allana.pokemonapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import id.allana.pokemonapp.databinding.FragmentDetailPokemonBinding
import id.allana.pokemonapp.model.response.PokemonDetail
import id.allana.pokemonapp.ui.PokemonViewModel
import java.io.File


class DetailPokemonFragment : Fragment() {

    private var _binding: FragmentDetailPokemonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonViewModel by viewModels()
    private val args by navArgs<DetailPokemonFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDetailPokemon(args.pokemonId)
        observeData()

        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menu_download) {
                Toast.makeText(context, "Download Photo!", Toast.LENGTH_SHORT).show()
                true
            } else false
        }
    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.contentDetailPokemon.visibility = View.GONE
                binding.sivPokemonPoster.visibility = View.GONE

                binding.tvLoadingData.visibility = View.VISIBLE
                binding.pbLoadPokemon.visibility = View.VISIBLE
            } else {
                binding.contentDetailPokemon.visibility = View.VISIBLE
                binding.sivPokemonPoster.visibility = View.VISIBLE

                binding.tvLoadingData.visibility = View.GONE
                binding.pbLoadPokemon.visibility = View.GONE
            }
        }

        viewModel.detailPokemon.observe(viewLifecycleOwner) { detailPokemon ->
            setDataDetail(detailPokemon.pokemonDetail)
        }
    }

    private fun setDataDetail(data: PokemonDetail) {
        binding.apply {
            Glide.with(requireContext())
                .load(data.images.large)
                .into(sivPokemonPoster)
            tvPokemonName.text = data.name
            tvPokemonType.text = data.supertype

        }
    }

    /**
     * -----NOT YET IMPLEMENTED-----
     * DOWNLOAD IMAGE TO LOCAL STORAGE
     */
    private fun downloadImage(imageUrl: String) {
        Glide.with(requireContext())
            .asFile()
            .load(imageUrl)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(
                    resource: File,
                    transition: Transition<in File>?
                ) {
                    saveImageToStorage(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun saveImageToStorage(file: File) {}
}