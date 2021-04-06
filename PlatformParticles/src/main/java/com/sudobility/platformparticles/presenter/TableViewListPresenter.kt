package com.sudobility.platformparticles.presenter

import android.content.Context
import android.text.Layout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class TableViewListPresenter(context: Context): RecycleViewListPresenter(context) {
    override val layoutManager: RecyclerView.LayoutManager?
        get() = LinearLayoutManager(context)

}