package com.aroman.mimwallet.presentation.wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aroman.mimwallet.databinding.BottomSheetInsertBinding
import com.aroman.mimwallet.databinding.FragmentWalletBinding
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.DisplayableItem
import com.aroman.mimwallet.domain.model.Insert
import com.aroman.mimwallet.presentation.wallet.adapters.MainWalletAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private val walletViewModel by viewModels<WalletViewModel>()
    private val portfolioAdapter = MainWalletAdapter(
        { position -> onItemClicked(position) },
        { position -> onInsertClicked(position) })

    private var coinList = listOf<DisplayableCoin>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecycler()
    }

    private fun initViewModel() {
        walletViewModel.coins.observe(viewLifecycleOwner) { coinList ->
            this.coinList = coinList
        }
//        walletViewModel.getCoins()
        walletViewModel.portfolio.observe(viewLifecycleOwner) { portfolio ->
            Log.d("@@@", portfolio.toString())

            val data = mutableListOf<DisplayableItem>().also {
                it.addAll(portfolio)
                it.add(Insert())
            }
            portfolioAdapter.items = data
            portfolioAdapter.notifyDataSetChanged()
        }
        walletViewModel.getPortfolio()
    }

    private fun initRecycler() {
        binding.recyclerViewCoin.adapter = portfolioAdapter
        binding.recyclerViewCoin.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        portfolioAdapter.items = listOf<DisplayableItem>(Insert())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onInsertClicked(position: Int) {
        showInsertDialog()
    }

    private fun showInsertDialog() {
        val dialogBinding = BottomSheetInsertBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogInsert = BottomSheetDialog(requireContext()).apply {
            setContentView(dialogBinding.root)
        }

        initSpinner(coinList.map { it.name + ": " + it.symbol }, dialogBinding.spinnerCoins)
        initSave(dialogInsert, dialogBinding, coinList)
        dialogInsert.show()
    }

    private fun initSave(
        dialogInsert: BottomSheetDialog,
        dialogBinding: BottomSheetInsertBinding,
        coinList: List<DisplayableCoin>
    ) {
        if (coinList.isNotEmpty()) {
            dialogBinding.buttonSave.setOnClickListener {
                val selectedSymbol = dialogBinding.spinnerCoins.selectedItem.toString().takeLast(3)
                walletViewModel.insertCoin(
                    DisplayableCoin(
                        id = coinList.find { it.symbol == selectedSymbol }!!.id,
                        name = coinList.find { it.symbol == selectedSymbol }!!.name,
                        symbol = selectedSymbol,
                        count = dialogBinding.editTextCoinAmount.text.toString().toDouble()
                    )
                )
                dialogInsert.hide()
                Snackbar.make(binding.root, "Saved", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSpinner(stringArray: List<String>, spinner: Spinner) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            stringArray
        ).also { adapter ->
            adapter.setDropDownViewResource(com.ismaeldivita.chipnavigation.R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun onItemClicked(position: Int) {

    }
}