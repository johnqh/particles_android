package com.sudobility.platformjedio.presenter.output

import android.content.Context
import com.sudobility.uitoolkits.shared.UICollectionView

public enum class MediaMode (val rawValue: Int) {
    normal(0), cards(1), fullScreen(2);

    companion object {
        operator fun invoke(rawValue: Int) = MediaMode.values().firstOrNull { it.rawValue == rawValue }
    }
}

public class ImagesFieldOutputPresenter(context: Context): FieldOutputPresenter(context) {
    override var collectionView: UICollectionView? = null
    var mediaMode: MediaMode = MediaMode.normal
}
