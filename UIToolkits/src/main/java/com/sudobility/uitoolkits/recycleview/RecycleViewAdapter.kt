package com.sudobility.uitoolkits.recycleview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


open class RecycleViewAdapter(
        private val context: Context,
        private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {
    public var list: List<Any>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // Returns the total count of items in the list
    override fun getItemCount(): Int = list?.size ?: 0

    open override fun getItemViewType(position: Int): Int {
        return 0
    }

    // Usually involves inflating a layout from XML and returning the holder - THIS IS EXPENSIVE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType != 0) {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return ViewHolder(view)
        } else {
            val presenterView = View(context)
            return ViewHolder(presenterView)
        }
    }

    // Involves populating data into the item through holder - NOT expensive
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let {
            val item = it[position]
            holder.bind(item)
        }
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: Any) {
        }
    }
}