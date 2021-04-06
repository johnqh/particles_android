package com.sudobility.particleskit.interactor.list

import com.sudobility.particleskit.presenter.`object`.SelectableProtocol


open class SelectableListInteractor: ListInteractor(), SelectableProtocol {
    override var isSelected: Boolean = false
}
