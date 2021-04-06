package com.sudobility.particlescommonmodels.navigationobjects

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import java.net.URL


public interface NavigationModelProtocol: ModelObjectProtocol {
    val title: String?
    val subtitle: String?
    val text: String?
    val subtext: String?
    val color: String?
    val icon: URL?
    val image: URL?
    val link: URL?
    val tag: String?
    val children: List<NavigationModelProtocol>?
    val actions: List<NavigationModelProtocol>?
}
