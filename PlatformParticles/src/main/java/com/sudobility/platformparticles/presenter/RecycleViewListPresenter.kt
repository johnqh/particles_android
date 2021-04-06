package com.sudobility.platformparticles.presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.presenter.list.ListPresenter


open abstract class RecycleViewListPresenter(val context: Context): ListPresenter() {
    abstract open val layoutManager: RecyclerView.LayoutManager?

    private val onClickListener: View.OnClickListener = View.OnClickListener { view ->
        selectionHandler?.select((view as? ObjectPresenterView)?.model)
    }

    private var recyclerViewAdapter = RecycleViewListPresenterAdapter(context, onClickListener)
    public var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            recyclerView?.adapter = recyclerViewAdapter
            recyclerView?.layoutManager = layoutManager
        }

    override fun update(items: List<ModelObjectProtocol>?) {
        super.update(items)
        recyclerViewAdapter.list = items
    }
}