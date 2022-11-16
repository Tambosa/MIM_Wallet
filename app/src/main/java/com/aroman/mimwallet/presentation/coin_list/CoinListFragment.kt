package com.aroman.mimwallet.presentation.coin_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aroman.mimwallet.databinding.FragmentCoinListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinListFragment : Fragment() {
    private var _binding: FragmentCoinListFragmentBinding? = null
    private val binding get() = _binding!!
    private val coinListViewModel by viewModels<CoinListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("@@@", "onViewCreated")
        coinListViewModel.coins.observe(viewLifecycleOwner) {
            Log.d("@@@", it.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}