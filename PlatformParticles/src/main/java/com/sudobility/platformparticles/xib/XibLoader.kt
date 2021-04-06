package com.sudobility.platformparticles.xib

import com.sudobility.utilities.json.JsonLoader
import org.json.JSONObject
import android.content.Context
import com.sudobility.platformparticles.cache.LayoutCache
import com.sudobility.platformparticles.presenter.ObjectPresenter
import com.sudobility.platformparticles.presenter.ObjectPresenterView
import com.sudobility.utilities.app.UtilsApplication

object XibLoader {
    private var json: JSONObject? = JsonLoader.load("xib.json")

    public fun xib(className: String): JSONObject? {
        /*
        JSON should contain a class name of the Object Presenter, and the XML name of the relative layout, layoutId for the layoutId
         */
        json?.let {
            val item = it[className]
            val string = item as? String
            string?.let {
                var json = JSONObject()
                json.put("presenter", string)
                return json
            }
            return item as? JSONObject
        }
        return null
    }

    public fun view(className: String): ObjectPresenterView? {
        val xib = xib(className)
        xib.let {  it ->
            val presenterClass = xib?.get("presenter") as? String
            val xml = xib?.get("xml") as? String
            xml?.let { it ->
                val layoutId = LayoutCache.layoutId(it)
                if (presenterClass != null && xml != null && layoutId != null) {
                    val presenter = Class.forName(presenterClass!!).newInstance() as? ObjectPresenter
                    presenter?.let {  it ->
                        val app = UtilsApplication.shared()
                        val presenterView = ObjectPresenterView(app)
                        presenterView.inflate(layoutId.toInt())
                        presenterView.presenter = presenter
                        return  presenterView
                    }
                }
            }
        }
        return  null
    }
}