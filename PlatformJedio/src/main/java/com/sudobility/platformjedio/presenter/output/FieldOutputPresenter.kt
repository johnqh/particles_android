package com.sudobility.platformjedio.presenter.output

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sudobility.jediokit.field.output.FieldOutput
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.presenter.`object`.ObjectPresenter
import com.sudobility.particleskit.presenter.`object`.ObjectTableCellPresenterProtocol
import com.sudobility.platformparticles.cache.LayoutCache
import com.sudobility.platformparticles.presenter.ObjectPresenterView
import com.sudobility.platformparticles.presenter.RecycleViewListPresenterAdapter
import com.sudobility.uitoolkits.image.UIImage
import com.sudobility.uitoolkits.image.image
import com.sudobility.uitoolkits.recycleview.RecycleViewAdapter
import com.sudobility.uitoolkits.shared.*
import com.sudobility.uitoolkits.view.TextCollectionViewCell
import com.sudobility.utilities.extensions.localized
import com.sudobility.utilities.extensions.replacingOccurrences


open class FieldOutputPresenter(private val context: Context) : ObjectPresenter(), ObjectTableCellPresenterProtocol {
    private val onClickListener: View.OnClickListener = View.OnClickListener { view ->
    }
    private var collectionViewAdapter = FieldOutputPresenterCollectionViewAdapter(context, onClickListener)

    internal val field: FieldOutput?
        get() = model as? FieldOutput

    internal var titleLabel: LabelProtocol? = null
    internal var subtitleLabel: LabelProtocol? = null
    internal var textLabel: LabelProtocol? = null
    internal var subtextLabel: LabelProtocol? = null
    internal var imageView: ImageView? = null
    open var collectionView: UICollectionView? = null
        set(value) {
            field = value
            collectionView?.adapter = collectionViewAdapter
            collectionView?.layoutManager = GridLayoutManager(context, 2)
        }

    override open val selectable: Boolean
        get() {
            return this.field?.link != null
        }

    var entity: ModelObjectProtocol? = null
        set(value) {
            val oldValue = field
            field = value
            changeObservation(from = oldValue, to = entity, keyPath = "data") { _, _, _ ->
                this.update()
            }
        }
    override var model: ModelObjectProtocol? = null
        set(value) {
            val oldValue = field
            field = value
            changeObservation(from = oldValue, to = field, keyPath = "entity") { _, _, _ ->
                this.entity = this.field?.entity
            }
            changeObservation(from = oldValue, to = field, keyPath = "field") { _, _, _ ->
                this.update()
            }
        }
    override val showDisclosure: Boolean
        get() = selectable

    override fun update() {
        titleLabel?.text = field?.title?.localized
        subtitleLabel?.text =
            field?.subtitle?.localized?.replacingOccurrences(of = "<br/>", with = "\n")
        textLabel?.text = field?.text?.localized
        subtextLabel?.text = field?.subtext?.localized
        val image = field?.image
        if (image != null) {
            imageView?.image = UIImage.named(image)
        } else {
            val checked = field?.checked
            if (checked != null) {
                imageView?.image = UIImage.named(if (checked) "checked_yes" else "checked_no")
            } else {
                imageView?.image = null
            }
        }
        collectionView?.reloadData()
    }
}

open class FieldOutputPresenterCollectionViewAdapter(context: Context, private val onClickListener: View.OnClickListener): RecycleViewAdapter(context, onClickListener) {
    public var field: FieldOutput? = null

    override fun getItemViewType(position: Int): Int {
        if (field?.strings != null) {
            return LayoutCache.layoutId("text_collection_cell") ?: 0
        } else if (field?.images != null) {
            return LayoutCache.layoutId("image_collection_cell") ?: 0
        } else {
            return 0
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return field?.strings?.count() ?: field?.images?.count() ?: 0
    }
}