package id.allana.pokemonapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.pokemonapp.adapter.ListPokemonAdapter
import id.allana.pokemonapp.adapter.LoadingStateAdapter
import id.allana.pokemonapp.adapter.SearchListPokemonAdapter
import id.allana.pokemonapp.databinding.FragmentListPokemonBinding
import id.allana.pokemonapp.ui.PokemonViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListPokemonFragment : Fragment() {

    private var _binding: FragmentListPokemonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonViewModel by viewModels()

    private val adapterPokemon: ListPokemonAdapter by lazy {
        ListPokemonAdapter()
    }

    private val adapterSearchPokemon: SearchListPokemonAdapter by lazy {
        SearchListPokemonAdapter()
    }

    private var debounceJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchPokemon()
        initRecyclerView()
        observeData()
    }

    private fun searchPokemon() {
        val searchView = binding.searchViewPokemon
        searchView.editText.addTextChangedListener(textWatcher)
    }

    private fun initRecyclerView() {
        binding.rvListPokemon.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterPokemon
        }

        binding.rvSearchPokemon.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterSearchPokemon
        }
    }


    private fun observeData() {
        binding.rvListPokemon.adapter = adapterPokemon.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapterPokemon.retry()
            }
        )

        /**
         * LIST
         */
        viewModel.listPokemon.observe(viewLifecycleOwner)  {
            adapterPokemon.submitData(lifecycle, it)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapterPokemon.loadStateFlow.collect {
                    binding.pbLoadPokemon.isVisible = it.refresh is LoadState.Loading
                }
            }
        }

        /**
         * SEARCH
         */

//        viewModel.sear.observe(viewLifecycleOwner) {
//            adapterSearchPokemon.submitData(lifecycle, it)
//        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbLoadSearchPokemon.apply {
                visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            debounceJob?.cancel()
            val query = p0.toString()
            debounceJob = lifecycleScope.launch {
                delay(1000L) // Sesuaikan dengan kebutuhan
                if (query.isNotEmpty()) {
                    viewModel.searchPokemon(query).observe(viewLifecycleOwner) {
                        adapterSearchPokemon.submitData(lifecycle, it)
                    }
                } else {
                    adapterSearchPokemon.submitData(PagingData.empty())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}