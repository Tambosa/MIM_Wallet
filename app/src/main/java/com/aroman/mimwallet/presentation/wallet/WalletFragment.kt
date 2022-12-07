package com.aroman.mimwallet.presentation.wallet

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.databinding.BottomSheetInsertBinding
import com.aroman.mimwallet.databinding.FragmentWalletBinding
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.DisplayableGettingStarted
import com.aroman.mimwallet.domain.model.DisplayableInsert
import com.aroman.mimwallet.domain.model.DisplayableItem
import com.aroman.mimwallet.presentation.wallet.adapters.MainWalletAdapter
import com.aroman.mimwallet.utils.*
import com.aroman.mimwallet.utils.pie_chart_view.PieData
import com.aroman.mimwallet.utils.theming.ThemeManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.hypot


@AndroidEntryPoint
class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val walletViewModel by viewModels<WalletViewModel>()
    private val portfolioAdapter = MainWalletAdapter(
        { position -> onItemClicked(position) },
        { onInsertClicked() })

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

    override fun onResume() {
        super.onResume()
        checkPrefsForTheme()
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPreferences.edit().putInt(NIGHT_MODE, 1).apply()
                setTheme(ThemeManager.Theme.DARK)
            } else {
                sharedPreferences.edit().putInt(NIGHT_MODE, 0).apply()
                setTheme(ThemeManager.Theme.LIGHT)
            }
        }
    }

    private fun checkPrefsForTheme() {
        sharedPreferences =
            requireActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        when (sharedPreferences.getInt(NIGHT_MODE, 0)) {
            0 -> {
                setTheme(ThemeManager.Theme.LIGHT, false)
                binding.darkModeSwitch.isSelected = false
            }
            else -> {
                setTheme(ThemeManager.Theme.DARK, false)
                binding.darkModeSwitch.isSelected = true
            }
        }
    }

    private fun setTheme(theme: ThemeManager.Theme, animate: Boolean = true) {
        if (!animate) {
            ThemeManager.theme = theme
            return
        }

        if (binding.shadowThemeImageView.isVisible) {
            return
        }

        requireActivity().disableTouch()
        val container = requireActivity().findViewById<ConstraintLayout>(R.id.container)

        val w = container.measuredWidth
        val h = container.measuredHeight

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        container.draw(canvas)

        binding.shadowThemeImageView.setImageBitmap(bitmap)
        binding.shadowThemeImageView.visibility = View.VISIBLE

        val finalRadius = hypot(w.toFloat(), h.toFloat())

        ThemeManager.theme = theme

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.shadowThemeImageView,
            w / 2,
            h / 2,
            finalRadius,
            0f
        )
        anim.duration = 400L
        anim.doOnEnd {
            binding.shadowThemeImageView.setImageDrawable(null)
            binding.shadowThemeImageView.visibility = View.GONE
            requireActivity().enableTouch()
        }
        anim.start()
    }

    private fun initViewModel() {
        subscribeToCoinList()
        subscribeToPortfolio()
    }

    private fun subscribeToCoinList() {
        walletViewModel.coins.observe(viewLifecycleOwner) { coinList ->
            when (coinList) {
                is ViewState.Success -> {
                    requireActivity().enableTouch()
                    this.coinList = coinList.data ?: emptyList()
                }
                is ViewState.Loading -> requireActivity().disableTouch()
                is ViewState.Error -> requireActivity().showMessage(coinList.message)
            }
        }
        walletViewModel.getCoins()
    }

    private fun subscribeToPortfolio() {
        walletViewModel.portfolio.observe(viewLifecycleOwner) { portfolio ->
            when (portfolio) {
                is ViewState.Success -> handleSuccessPortfolio(portfolio)
                is ViewState.Loading -> {}
                is ViewState.Error -> requireActivity().showMessage(portfolio.message)
            }
        }
        walletViewModel.getPortfolio()
    }

    private fun handleSuccessPortfolio(portfolio: ViewState.Success<List<DisplayableCoin>>) {
        Log.d("@@@", portfolio.toString())
        setHeader(portfolio.data ?: emptyList())
        val recyclerList = mutableListOf<DisplayableItem>().also {
            it.addAll(portfolio.data ?: emptyList())
            it.add(DisplayableInsert)
        }
        portfolioAdapter.items = recyclerList
        portfolioAdapter.notifyDataSetChanged()

        portfolio.data?.let { initPieChart(it) }
    }

    private fun initPieChart(portfolio: List<DisplayableCoin>) {
        val pieData = PieData()
        for (coin in portfolio) {
            pieData.add(coin.symbol, coin.price * coin.count)
        }
        binding.pieChart.setData(pieData)
    }

    private fun setHeader(portfolio: List<DisplayableCoin>) {
        var totalPrice = 0.0
        var oldTotalPrice = 0.0
        for (coin in portfolio) {
            totalPrice += coin.count * coin.price
            val differ = (coin.count * coin.price * (coin.percentChange24h / 100))
            oldTotalPrice += ((coin.count * coin.price) + differ)
        }
        val change = ((oldTotalPrice - totalPrice) / totalPrice) * 100

        val priceFormat = DecimalFormat("$#.##")
        priceFormat.roundingMode = RoundingMode.CEILING
        binding.textTotalValue.animateNumbers(2500, totalPrice, priceFormat)

        val gainFormat = DecimalFormat("0.##'%'")
        gainFormat.roundingMode = RoundingMode.CEILING
        binding.text24hGain.text = gainFormat.format(change)
        if (change > 0) {
            binding.text24hGain.setTextColor(Color.GREEN)
        } else {
            binding.text24hGain.setTextColor(Color.RED)
        }
    }

    private fun initRecycler() {
        binding.recyclerViewCoin.apply {
            adapter = portfolioAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            attachLeftSwipeHelper { vh ->
                walletViewModel.deleteCoin(
                    walletViewModel.portfolio.value!!.data!![vh.layoutPosition]
                )
            }
        }
        portfolioAdapter.items =
            listOf<DisplayableItem>(DisplayableGettingStarted, DisplayableInsert)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onInsertClicked() {
        showInsertDialog()
    }

    private fun showInsertDialog(symbol: String = "", qnt: Double = 1.0) {
        val dialogBinding = BottomSheetInsertBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogInsert = BottomSheetDialog(requireContext()).apply {
            setContentView(dialogBinding.root)
        }

        initAutoCompleteCoin(coinList.map { it.name + ": " + it.symbol }, dialogBinding)
        dialogBinding.autocompleteCoin.setText(symbol)
        initEditTextCoinAmount(dialogBinding, qnt)
        initSave(dialogInsert, dialogBinding, coinList)

        dialogInsert.show()
    }

    private fun initEditTextCoinAmount(dialogBinding: BottomSheetInsertBinding, qnt: Double) {
        dialogBinding.editTextCoinAmount.setText(qnt.toString(), TextView.BufferType.EDITABLE)
        dialogBinding.editTextCoinAmount.addTextChangedListener { enteredText ->
            if (enteredText != null) {
                if (enteredText.isNotEmpty() && enteredText.toString().toDouble() != 0.0) {
                    dialogBinding.buttonSave.isEnabled = true
                    dialogBinding.qntInputLayout.boxStrokeColor = (Color.GREEN)
                    dialogBinding.qntInputLayout.error = null
                } else {
                    dialogBinding.buttonSave.isEnabled = false
                    dialogBinding.qntInputLayout.boxStrokeColor = (Color.RED)
                    dialogBinding.qntInputLayout.error = "Enter valid amount"
                }
            }
        }
    }

    private fun initAutoCompleteCoin(
        stringArray: List<String>,
        dialogBinding: BottomSheetInsertBinding
    ) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            stringArray
        ).also { adapter ->
            dialogBinding.autocompleteCoin.setAdapter(adapter)
        }
        dialogBinding.autocompleteCoin.addTextChangedListener { enteredText ->
            if (coinList.map { it.symbol }
                    .contains(enteredText?.takeLastWhile { it != ' ' }.toString())) {
                dialogBinding.buttonSave.isEnabled = true
                dialogBinding.coinInputLayout.boxStrokeColor = (Color.GREEN)
                dialogBinding.coinInputLayout.error = null
            } else {
                dialogBinding.buttonSave.isEnabled = false
                dialogBinding.coinInputLayout.error = "Enter valid coin"
            }
        }
    }

    private fun initSave(
        dialogInsert: BottomSheetDialog,
        dialogBinding: BottomSheetInsertBinding,
        coinList: List<DisplayableCoin>
    ) {
        if (coinList.isNotEmpty()) {
            dialogBinding.buttonSave.setOnClickListener {
                val selectedSymbol =
                    dialogBinding.autocompleteCoin.text.toString().takeLastWhile { it != ' ' }
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

    private fun onItemClicked(position: Int) {
        showInsertDialog(
            walletViewModel.portfolio.value!!.data!![position].symbol,
            walletViewModel.portfolio.value!!.data!![position].count
        )
    }

    companion object {
        const val NIGHT_MODE = "night mode"
    }
}