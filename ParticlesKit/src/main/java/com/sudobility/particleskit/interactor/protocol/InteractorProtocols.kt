package com.sudobility.particleskit.interactor.protocol

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.routingkit.router.RoutingRequest


public interface ActionProtocol: ModelObjectProtocol {
    val title: String?
    val subtitle: String?
    val detail: String?
    val image: String?
    val routing: RoutingRequest?
    val detailRouting: RoutingRequest?
}

public interface InteractorProtocol: ModelObjectProtocol {
    var entity: ModelObjectProtocol?
}

public interface LoadingInteractorProtocol: InteractorProtocol {
    var objectKey: String?
}
