package com.sudobility.platformjedio.presenter.input.list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sudobility.jediokit.field.FieldInputDefinition
import com.sudobility.jediokit.field.input.FieldInput
import com.sudobility.jediokit.field.output.FieldOutput
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.platformjedio.presenter.input.FieldInputPresenter
import com.sudobility.platformjedio.presenter.output.FieldOutputPresenterCollectionViewAdapter
import com.sudobility.platformparticles.cache.LayoutCache
import com.sudobility.uitoolkits.recycleview.RecycleViewAdapter
import com.sudobility.uitoolkits.shared.reloadData


open class FieldListInputPresenter(private val context: Context): FieldInputPresenter() {
    var options: List<Map<String, Any>>? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            tableView?.reloadData()
        }

    private val onClickListener: View.OnClickListener = View.OnClickListener { view ->
    }

    private var tableViewAdapter = FieldListInputPresenterTableViewAdapter(context, onClickListener)

    open val selectionCellXib: String
        get() = "text_table_cell"
    override open var model: ModelObjectProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = field, keyPath = "field") { _, _, _  ->
                this.definition = this.field?.field as? FieldInputDefinition
            }
        }
    private var definition: FieldInputDefinition? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = definition, keyPath = "options") { _, _, _  ->
                this.options = this.definition?.options
            }
        }

    open var tableView: RecyclerView? = null
        set(value) {
            field = value
            tableView?.adapter = tableViewAdapter
            tableView?.layoutManager = LinearLayoutManager(context)
        }

    override open fun update() {
        super.update()
        val tableView = tableView
        val options = field?.options
        if (tableView != null && options != null) {
//            tableView.reloadData()
//            for ((index, option) in options.enumerated()) {
//                if (isSelected(value = option["value"])) {
//                    val indexPath = IndexPath(row = index, section = 0)
//                    tableView.selectRow(at = indexPath, animated = false, scrollPosition = .middle)
//                }
//            }
        }
    }

    open fun isSelected(value: Any?) : Boolean =
        false

    open fun select(value: Any?) {}

    open fun deselect(value: Any?) {}
}

open class FieldListInputPresenterTableViewAdapter(context: Context, private val onClickListener: View.OnClickListener): RecycleViewAdapter(context, onClickListener) {
    public var field: FieldInput? = null

    override fun getItemViewType(position: Int): Int {
        if (field?.options != null) {
            return LayoutCache.layoutId("text_table_cell") ?: 0
        } else {
            return 0
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return field?.options?.count() ?: 0
    }
}