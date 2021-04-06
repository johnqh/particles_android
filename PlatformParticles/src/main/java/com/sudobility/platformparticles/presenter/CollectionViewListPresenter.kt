package com.sudobility.platformparticles.presenter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class CollectionViewListPresenter(context: Context): RecycleViewListPresenter(context) {
    override val layoutManager: RecyclerView.LayoutManager?
        get() = GridLayoutManager(context, 2)

}