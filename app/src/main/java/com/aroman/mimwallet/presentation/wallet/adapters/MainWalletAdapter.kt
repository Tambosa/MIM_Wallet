package com.aroman.mimwallet.presentation.wallet.adapters

import com.aroman.mimwallet.domain.model.DisplayableItem
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class MainWalletAdapter(
    onItemClicked: (position: Int) -> Unit,
    onInsertClicked: (position: Int) -> Unit,
) : ListDelegationAdapter<List<DisplayableItem>>(
    coinAdapterDelegate(onItemClicked),
    insertAdapterDelegate(onInsertClicked)
)