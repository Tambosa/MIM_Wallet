package com.aroman.mimwallet.presentation.wallet

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
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
import com.jraska.falcon.Falcon
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
        restoreTheme()
        initThemeSwitch()
    }

    //region theme

    private fun restoreTheme() {
        sharedPreferences =
            requireActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        when (sharedPreferences.getInt(NIGHT_MODE, 0)) {
            0 -> {
                ThemeManager.theme = ThemeManager.Theme.LIGHT
                binding.darkModeButton.background =
                    resources.getDrawable(R.drawable.ic_baseline_nights_stay_24, null)
            }
            else -> {
                ThemeManager.theme = ThemeManager.Theme.DARK
                binding.darkModeButton.background =
                    resources.getDrawable(R.drawable.ic_baseline_wb_sunny_24, null)
            }
        }
    }

    private fun initThemeSwitch() {
        binding.darkModeButton.setOnClickListener {
            when (ThemeManager.theme) {
                ThemeManager.Theme.DARK -> {
                    setTheme(ThemeManager.Theme.LIGHT)
                    sharedPreferences.edit().putInt(NIGHT_MODE, 0).apply()
                }
                ThemeManager.Theme.LIGHT -> {
                    setTheme(ThemeManager.Theme.DARK)
                    sharedPreferences.edit().putInt(NIGHT_MODE, 1).apply()
                }
            }
        }
    }

    private fun setTheme(theme: ThemeManager.Theme) {
        initScreenShot()
        ThemeManager.theme = theme
        initThemeAnimations()
    }

    private fun initScreenShot() {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        val windowBitmap = Falcon.takeScreenshotBitmap(activity)
        val bitmap = Bitmap.createBitmap(
            windowBitmap,
            0,
            statusBarHeight,
            binding.container.measuredWidth,
            binding.container.measuredHeight,
            null,
            true
        )
        binding.shadowThemeImageView.setImageBitmap(bitmap)
        binding.shadowThemeImageView.visibility = View.VISIBLE
    }

    private fun initThemeAnimations() {
        binding.darkModeButton.startAnimation(
            RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                duration = 1000L
                interpolator = LinearInterpolator()
            }
        )

        ViewAnimationUtils.createCircularReveal(
            binding.shadowThemeImageView,
            binding.darkModeButton.x.toInt() + 30,
            binding.darkModeButton.y.toInt() + 30,
            hypot(
                binding.container.measuredWidth.toFloat(),
                binding.container.measuredHeight.toFloat()
            ),
            0f
        ).apply {
            duration = 800L
            doOnStart {
                requireActivity().disableTouch()
            }
            doOnEnd {
                binding.shadowThemeImageView.setImageDrawable(null)
                binding.shadowThemeImageView.visibility = View.GONE
                when (ThemeManager.theme) {
                    ThemeManager.Theme.DARK -> binding.darkModeButton.background =
                        resources.getDrawable(R.drawable.ic_baseline_wb_sunny_24, null)
                    ThemeManager.Theme.LIGHT -> binding.darkModeButton.background =
                        resources.getDrawable(R.drawable.ic_baseline_nights_stay_24, null)
                }
                requireActivity().enableTouch()
            }
            start()
        }
    }

    //endregion

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
        val recyclerList = mutableListOf<DisplayableItem>()
        if (portfolio.data.isNullOrEmpty()) {
            binding.textTotalValue.visibility = View.GONE
            binding.text24hGain.visibility = View.GONE
            binding.pieChart.visibility = View.GONE
            recyclerList.add(DisplayableGettingStarted)
            recyclerList.add(DisplayableInsert)
        } else {
            setHeader(portfolio.data)
            recyclerList.addAll(portfolio.data)
            recyclerList.add(DisplayableInsert)
            initPieChart(portfolio.data)
        }

        portfolioAdapter.items = recyclerList
        portfolioAdapter.notifyDataSetChanged()
    }

    private fun initPieChart(portfolio: List<DisplayableCoin>) {
        binding.pieChart.visibility = View.VISIBLE
        val pieData = PieData()
        for (coin in portfolio) {
            pieData.add(coin.symbol, coin.price * coin.count)
        }
        binding.pieChart.setData(pieData)
    }

    private fun setHeader(portfolio: List<DisplayableCoin>) {
        binding.textTotalValue.visibility = View.VISIBLE
        binding.text24hGain.visibility = View.VISIBLE
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
            viewTreeObserver.addOnGlobalLayoutListener {
                restoreTheme()
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

        dialogInsert.window?.setDimAmount(0f)

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