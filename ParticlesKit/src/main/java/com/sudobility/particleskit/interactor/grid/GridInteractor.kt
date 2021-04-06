package com.sudobility.particleskit.interactor.grid

import com.sudobility.particleskit.entity.model.ModelGridProtocol
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.protocol.InteractorProtocol
import com.sudobility.utilities.bridge.DispatchQueue
import com.sudobility.utilities.kvo.NSObject


open class GridInteractor: NSObject(), InteractorProtocol, ModelGridProtocol {
    override val width: Int
        get() = grid?.size ?: 0
    override val height: Int
        get() = grid?.firstOrNull()?.size ?: 0
    override var entity: ModelObjectProtocol? = null
    open var loading: Boolean = false
    override val displayTitle: String?
        get() = entity?.displayTitle ?: null
    override val displaySubtitle: String?
        get() = entity?.displaySubtitle ?: null
    override val displayImageUrl: String?
        get() = entity?.displayImageUrl ?: null
    override var grid: List<List<ModelObjectProtocol>>? = null

    private fun indexOf(sourceObject: ModelObjectProtocol?, inside: List<ModelObjectProtocol>?, startIndex: Int) : Int? {
        return inside?.indexOf(sourceObject)
    }

    private fun compare(destination: ModelObjectProtocol?, source: ModelObjectProtocol?) : Boolean {
        return (destination === source)
    }

    open fun sync(grid: List<List<ModelObjectProtocol>>?) {
        DispatchQueue.runInMainThread {
            this.grid = grid
        }
    }
}
