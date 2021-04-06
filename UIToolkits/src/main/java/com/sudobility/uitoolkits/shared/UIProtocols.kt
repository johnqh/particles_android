package com.sudobility.uitoolkits.shared

import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Size
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.sudobility.uitoolkits.image.ImageCache
import com.sudobility.utilities.app.UtilsApplication
import java.text.AttributedString

typealias NativeColor = Color
typealias NativeFont = Typeface
typealias NativeImage = Image

typealias UICollectionView = RecyclerView
typealias UITableView = RecyclerView
typealias UICollectionViewCell = View
typealias UITableViewCell = View

typealias CGFloat = Float
typealias CGSize = Size

public interface ViewProtocol {
    var visible: Boolean
    var frame: Rect
    var backgroundColor: NativeColor
}

public interface SpinnerProtocol: ViewProtocol {
    var spinning: Boolean
}

public interface LabelProtocol: ViewProtocol {
    var attributedText: AttributedString?
    var text: String?
    var textColor: Int
    var font: NativeFont
}

public interface ImageViewProtocol: ViewProtocol {
    var image: NativeImage?
    var imageUrl: String?
}

public interface ControlProtocol: ViewProtocol {
    fun removeTarget()
    fun addTarget(target: Any?, action: View.OnClickListener)
}

public interface ButtonProtocol: ControlProtocol {
    var buttonTitle: String?
    var buttonImage: NativeImage?
    var buttonChecked: Boolean
}

public interface SegmentedProtocol: ControlProtocol {
    val numberOfSegments: Int
    var selectedIndex: Int
    fun segments(titles: List<String>) : SegmentedProtocol
}

interface WaitProtocol: ViewProtocol {
    var waiting: Boolean
}

public var View.visible: Boolean
    get() = this.isVisible
    set(value) {
        this.isVisible = value
    }

public var TextView.font: Typeface?
    get() = this.typeface
    set(value) {
        this.typeface = value
    }

public var View.backgroundColor: Int?
    get() = null
    set(value) {
    }

public var View.borderColor: Int?
    get() = null
    set(value) {
    }

public object UIColor {
    public val black: Int
        get() = Color.BLACK

    public val red: Int
        get() = Color.RED

    public val blue: Int
        get() = Color.BLUE

    public val green: Int
        get() = Color.GREEN

    public val white: Int
        get() = Color.WHITE

    public val clear: Int
        get() = Color.TRANSPARENT

    public fun color(hex: String): Int {
        return Color.parseColor(hex)
    }

}

public fun RecyclerView.reloadData() {
    this.adapter?.notifyDataSetChanged()
}