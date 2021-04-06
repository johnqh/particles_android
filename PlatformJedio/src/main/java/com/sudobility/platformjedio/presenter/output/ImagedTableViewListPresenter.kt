package com.sudobility.platformjedio.presenter.output

import android.content.Context
import com.sudobility.platformparticles.presenter.TableViewListPresenter


open class ImagedTableViewListPresenter(context: Context): TableViewListPresenter(context) {
    open var mediaPresenter: ImagesFieldOutputPresenter? = null
        set(value) {
            val oldValue = field
            field = value
            changeObservation(from = oldValue, to = mediaPresenter, keyPath = "mediaMode") { _, _, _  ->
                this.mediaMode = this.mediaPresenter?.mediaMode ?: MediaMode.normal
            }
        }
    open var mediaMode: MediaMode = MediaMode.normal

//    open override fun tableView(tableView: RecyclerView, indexPath: IndexPath) : UITableViewCell {
//        val cell = super.tableView(tableView, cellForRowAt = indexPath)
//        val presenterCell = tableView.cellForRow(at = IndexPath(row = 0, section = 0)) as? ObjectPresenterTableViewCell
//        val view = presenterCell.presenterView as? ObjectPresenterView
//        val presenter = view.presenter as? ImagesFieldOutputPresenter
//        if (presenterCell != null && view != null && presenter != null) {
//            mediaPresenter = presenter
//        }
//        return cell
//    }
}
