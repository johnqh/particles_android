package com.sudobility.uitoolkits.color

import com.sudobility.uitoolkits.shared.UIColor
import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.parser.Parser


final public class ColorPalette: NSObject {
    companion object {
        var shared: ColorPalette = { ColorPalette() }()
        var parserOverwrite: Parser? = null
    }

    override val parser: Parser
        get() = ColorPalette.parserOverwrite ?: super.parser
    private var colors: MutableMap<String, Any>? = null
    private var cache: MutableMap<String, Int> = mutableMapOf()

    constructor() : super() {
        colors = (JsonLoader.load(file = "colors.json") as? Map<String, Any>)?.toMutableMap()
    }

    fun color(text: String?) : Int? {
        val text = text
        if (text != null) {
            val color = UIColor.color(hex = text)
            if (color != null) {
                return color
            } else {
                return colorKeyed(text)
            }
        } else {
            return null
        }
    }

    fun colorKeyed(colorText: String?) : Int? {
        if (colorText != null) {
            val color = cache[colorText]
            if (color != null) {
                return color
            } else {
                val hex = hex(colorText)
                if (hex != null) {
                    val color = UIColor.color(hex = hex)
                    cache[colorText!!] = color
                    return color
                } else {
                    val color = UIColor.color(hex = colorText)
                    if (color != null) {
                        cache[colorText!!] = color
                        return color
                    } else {
                        val color = UIColor.red
                        cache[colorText!!] = color
                        return color
                    }
                }
            }
        }
        return null
    }

    fun colorSystem(colorText: String?) : Int? {
        return when (colorText) {
            "label" -> colorKeyed("black")
            "secondary" -> colorKeyed("gray")
            "dark" -> colorKeyed("darkGray")
            "gray" -> colorKeyed("gray")
            "superlight" -> colorKeyed("gray0")
            "light" -> colorKeyed("gray2")
            "quaternary" -> colorKeyed("lightGray")
            "disabled" -> colorKeyed("lightGray")
            "background" -> colorKeyed( "white")
            "clear" -> UIColor.clear
            else -> colorKeyed(colorText)
        }
    }

    fun hex(color: String) : String? =
            parser.asString(colors?.get(color))

}
