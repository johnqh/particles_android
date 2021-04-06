package com.sudobility.platformjedio.presenter.input.optiongrid

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sudobility.jediokit.field.input.FieldInput
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.platformjedio.presenter.input.FieldInputPresenter
import com.sudobility.platformjedio.presenter.input.list.FieldListInputPresenterTableViewAdapter
import com.sudobility.platformparticles.cache.LayoutCache
import com.sudobility.uitoolkits.recycleview.RecycleViewAdapter
import com.sudobility.uitoolkits.shared.CGFloat
import com.sudobility.uitoolkits.shared.UICollectionView


open class FieldGridInputPresenter(private val context: Context): FieldInputPresenter() {
    private val onClickListener: View.OnClickListener = View.OnClickListener { view ->
    }

    private var collectionViewAdapter = FieldGridInputPresenterCollectionViewAdapter(context, onClickListener)

    var cellXib: String? = null
    var cellHeight: CGFloat = 36.0F
    val validatedItemsPerRow: Int = 2
    var options: List<Map<String, Any>>? = null
    open val selectionCellXib: String
        get() = cellXib ?: "text_collection_cell"
    override open var model: ModelObjectProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            options = this.field?.options
        }

    open var collectionView: UICollectionView? = null
        set(value) {
            field = value
            collectionView?.adapter = collectionViewAdapter
            collectionView?.layoutManager = GridLayoutManager(context, 2)
        }

    override open fun update() {
        super.update()
    }

    open fun isSelected(value: Any?) : Boolean =
        false

    open fun select(value: Any?) {}

    open fun deselect(value: Any?) {}
}

open class FieldGridInputPresenterCollectionViewAdapter(context: Context, private val onClickListener: View.OnClickListener): RecycleViewAdapter(context, onClickListener) {
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