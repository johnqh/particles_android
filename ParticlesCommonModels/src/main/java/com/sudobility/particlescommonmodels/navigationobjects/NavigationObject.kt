package com.sudobility.particlescommonmodels.navigationobjects

import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.utilities.extensions.append
import java.net.URL


public class NavigationObject: DictionaryEntity(), NavigationModelProtocol {
    public val type: String?
        get() = parser.asString(data?.get("type"))
    public override val title: String?
        get() = parser.asString(data?.get("title"))
    public override val subtitle: String?
        get() = parser.asString(data?.get("subtitle"))
    public override val text: String?
        get() = parser.asString(data?.get("text"))
    public override val subtext: String?
        get() = parser.asString(data?.get("subtext"))
    public override val color: String?
        get() = parser.asString(data?.get("color"))
    public override val icon: URL?
        get() = parser.asURL(data?.get("icon"))
    public override val image: URL?
        get() = parser.asURL(data?.get("image"))
    public override val link: URL?
        get() = parser.asURL(data?.get("link"))
    public override val tag: String?
        get() = parser.asString(data?.get("tag"))
    public override var children: List<NavigationModelProtocol>? = null
    public override var actions: List<NavigationModelProtocol>? = null

    override fun parse(dictionary: Map<String, Any>) {
        super.parse(dictionary = dictionary)
        val childrenData = parser.asArray(dictionary["children"]) as? List<Map<String, Any>>
        if (childrenData != null) {
            var children = mutableListOf<NavigationModelProtocol>()
            for (childData in childrenData) {
                val child = NavigationObject()
                child.parse(dictionary = childData)
                children.append(child)
            }
            this.children = children
        } else {
            children = null
        }
        val actionsData = parser.asArray(dictionary["actions"]) as? List<Map<String, Any>>
        if (actionsData != null) {
            var actions = mutableListOf<NavigationModelProtocol>()
            for (actionData in actionsData) {
                val action = NavigationObject()
                action.parse(dictionary = actionData)
                actions.append(action)
            }
            this.actions = actions
        } else {
            actions = null
        }
    }
}
