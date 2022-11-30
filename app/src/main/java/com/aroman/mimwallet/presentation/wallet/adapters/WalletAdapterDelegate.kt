package com.aroman.mimwallet.presentation.wallet.adapters

import android.graphics.Color
import com.aroman.mimwallet.databinding.ItemCoinBinding
import com.aroman.mimwallet.databinding.ItemInsertBinding
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.DisplayableItem
import com.aroman.mimwallet.domain.model.Insert
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
            binding.singleCoinPrice.text = DecimalFormat("$#,###,###.00").format(item.price)
            binding.totalPrice.text = DecimalFormat("$#,###,###.00").format(item.price * item.count)
            binding.totalPrice24hChange.text = item.percentChange24h.toString()

            if (item.percentChange24h > 0) {
                binding.totalPrice24hChange.setTextColor(Color.GREEN)
            } else {
                binding.totalPrice24hChange.setTextColor(Color.RED)
            }
        }
    }

fun insertAdapterDelegate(
    onItemClicked: (position: Int) -> Unit
) =
    adapterDelegateViewBinding<Insert, DisplayableItem, ItemInsertBinding>({ layoutInflater, root ->
        ItemInsertBinding.inflate(layoutInflater, root, false)
    }) {
        bind {
            binding.buttonAddNewCoin.setOnClickListener {
                onItemClicked(layoutPosition)
            }
        }
    }