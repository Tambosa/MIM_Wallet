package com.aroman.mimwallet.presentation.wallet.adapters

import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.DisplayableItem
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class MainWalletAdapter(
    onItemClicked: (position: Int) -> Unit,
    onChipSelected: (DisplayableCoin) -> Double,
    onInsertClicked: () -> Unit,
) : ListDelegationAdapter<List<DisplayableItem>>(
    coinAdapterDelegate(onItemClicked, onChipSelected),
    insertAdapterDelegate(onInsertClicked),
    gettingStartedAdapterDelegate()
)