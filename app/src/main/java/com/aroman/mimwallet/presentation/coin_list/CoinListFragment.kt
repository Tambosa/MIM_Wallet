package com.aroman.mimwallet.presentation.coin_list

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aroman.mimwallet.databinding.FragmentCoinListFragmentBinding
import com.aroman.mimwallet.presentation.coin_details.CoinDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinListFragment : Fragment() {
    private var _binding: FragmentCoinListFragmentBinding? = null
    private val binding get() = _binding!!
    private val coinListViewModel by viewModels<CoinListViewModel>()
    private val coinDetailsViewModel by viewModels<CoinDetailsViewModel>()

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
        initViewModel()
    }

    private fun initViewModel() {
        coinListViewModel.coins.observe(viewLifecycleOwner) { coinList ->
            Log.d("@@@", coinList.toString())
            coinDetailsViewModel.coinDetails.observe(viewLifecycleOwner) { coinDetails ->
                Log.d("@@@", "\n" + coinDetails.toString())
            }
            coinDetailsViewModel.getCoinDetails(coinList[0].symbol)
            coinDetailsViewModel.getMultipleCoinDetails(coinList[0].symbol, coinList[1].symbol, coinList[2].symbol)
        }
        coinListViewModel.getCoins()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}