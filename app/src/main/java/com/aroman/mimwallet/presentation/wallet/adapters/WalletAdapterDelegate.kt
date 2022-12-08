package com.aroman.mimwallet.presentation.wallet.adapters

import android.graphics.Color
import com.aroman.mimwallet.databinding.ItemCoinBinding
import com.aroman.mimwallet.databinding.ItemGettingStartedBinding
import com.aroman.mimwallet.databinding.ItemInsertBinding
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.DisplayableGettingStarted
import com.aroman.mimwallet.domain.model.DisplayableInsert
import com.aroman.mimwallet.domain.model.DisplayableItem
import com.aroman.mimwallet.utils.animateNumbers
import com.aroman.mimwallet.utils.theming.DynamicLayoutInflater
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import java.text.DecimalFormat

fun coinAdapterDelegate(
    onItemClicked: (position: Int) -> Unit,
) =
    adapterDelegateViewBinding<DisplayableCoin, DisplayableItem, ItemCoinBinding>({ layoutInflater, root ->
        ItemCoinBinding.inflate(layoutInflater, root, false)
    }) {
        binding.itemCoin.setOnClickListener {
            onItemClicked(layoutPosition)
        }
        bind {
            binding.coinName.text = item.name
            binding.coinCount.text = "${item.count} ${item.symbol}"
            binding.totalPrice.animateNumbers(
                2000,
                item.price * item.count,
                DecimalFormat("$#,###,###.00")
            )
            binding.totalPrice24hChange.text =
                DecimalFormat("0.##'%'").format(item.percentChange24h)

            if (item.percentChange24h > 0) {
                binding.totalPrice24hChange.setTextColor(Color.GREEN)
            } else {
                binding.totalPrice24hChange.setTextColor(Color.RED)
            }
        }
    }

fun insertAdapterDelegate(
    onItemClicked: () -> Unit
) =
    adapterDelegateViewBinding<DisplayableInsert, DisplayableItem, ItemInsertBinding>({ layoutInflater, root ->
        ItemInsertBinding.inflate(layoutInflater, root, false)
    }) {
        bind {
            binding.buttonAddNewCoin.setOnClickListener {
                onItemClicked()
            }
        }
    }

fun gettingStartedAdapterDelegate() =
    adapterDelegateViewBinding<DisplayableGettingStarted, DisplayableItem, ItemGettingStartedBinding>(
        { layoutInflater, root ->
            ItemGettingStartedBinding.inflate(layoutInflater, root, false)
        }) {
        bind {}
    }