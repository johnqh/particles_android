package com.sudobility.utilities.bridge

import android.content.Context
import android.content.SharedPreferences
import com.sudobility.utilities.app.UtilsApplication
import java.net.URL

public class UserDefaults {
    companion object {
        public var standard = UserDefaults()
        private var rootKey = "preferences"
        private var prefs = UtilsApplication.shared().getSharedPreferences(
            rootKey, Context.MODE_PRIVATE
        )
    }

    public fun string(forKey: String): String? {
        return prefs?.getString(forKey, null)
    }

    public fun set(string: String, forKey: String) {
        set(prefs, string, forKey)
    }

    private fun set(prefs: SharedPreferences, string: String, forKey: String) {
        val editor = prefs.edit()
        set(editor, string, forKey)
        editor.commit()
    }

    private fun set(editor: SharedPreferences.Editor, string: String, forKey: String) {
        editor.putString(forKey, string)
    }

    public  fun url(forKey: String): URL? {
        val string = string(forKey)
        string?.let {
            return URL(string)
        }
        return null
    }

    public fun set(url: URL, forKey: String) {
        set(prefs, url, forKey)
    }

    private fun set(prefs: SharedPreferences, url: URL, forKey: String) {
        val editor = prefs.edit()
        set(editor, url, forKey)
        editor.commit()
    }

    private fun set(editor: SharedPreferences.Editor, url: URL, forKey: String) {
        editor.putString(forKey, url.toString())
    }

    public fun bool(forKey: String): Boolean {
        return prefs.getBoolean(forKey, false)
    }

    public fun set(boolean: Boolean, forKey: String) {
        set(prefs, boolean, forKey)
    }

    private fun set(prefs: SharedPreferences, boolean: Boolean, forKey: String) {
        val editor = prefs.edit()
        set(editor, boolean, forKey)
        editor.commit()
    }

    private fun set(editor: SharedPreferences.Editor, boolean: Boolean, forKey: String) {
        editor.putBoolean(forKey, boolean)
    }
    public fun integer(forKey: String): Int {
        return prefs.getInt(forKey, 0)
    }

    public fun set(int: Int, forKey: String) {
        set(prefs, int, forKey)
    }

    private fun set(prefs: SharedPreferences, int: Int, forKey: String) {
        val editor = prefs.edit()
        set(editor, int, forKey)
        editor.commit()
    }

    private fun set(editor: SharedPreferences.Editor, int: Int, forKey: String) {
        editor.putInt(forKey, int)
    }

    public fun float(forKey: String): Float {
        return prefs.getFloat(forKey, 0.0F)
    }

    public fun set(float: Float, forKey: String) {
        set(prefs, float, forKey)
    }

    private fun set(prefs: SharedPreferences, float: Float, forKey: String) {
        val editor = prefs.edit()
        set(editor, float, forKey)
        editor.commit()
    }

    private fun set(editor: SharedPreferences.Editor, float: Float, forKey: String) {
        editor.putFloat(forKey, float)
    }

    public fun double(forKey: String): Double {
        return prefs.getFloat(forKey, 0.0F).toDouble()
    }

    public fun set(double: Double, forKey: String) {
        set(prefs, double, forKey)
    }

    private fun set(prefs: SharedPreferences, double: Double, forKey: String) {
        val editor = prefs.edit()
        set(editor, double, forKey)
        editor.commit()
    }

    private fun set(editor: SharedPreferences.Editor, double: Double, forKey: String) {
        editor.putFloat(forKey, double.toFloat())
    }

    public fun stringArray(forKey: String): List<String>? {
        return prefs.getStringSet(forKey, null)?.toList()
    }

    public fun dictionary(forKey: String): MutableMap<String, Any>? {
        val key = "${rootKey}.${forKey}"
        val prefs = UtilsApplication.shared().getSharedPreferences(
            key, Context.MODE_PRIVATE
        )
        val all = prefs?.all
        all?.let {
            val map = mutableMapOf<String, Any>()
            for (key in all.keys) {
                map[key] = all[key] as Any
            }
            return map
        }
        return null
    }

    public fun set(any: Any?, forKey: String) {
        set(prefs, any, forKey)
    }

    public fun set(prefs: SharedPreferences, any: Any?, forKey: String) {
        if (any != null) {
            val map = any as? Map<String, *>
            if (map != null) {
                val key = "${rootKey}.${forKey}"
                val prefs = UtilsApplication.shared().getSharedPreferences(
                    key, Context.MODE_PRIVATE
                )
                val editor = prefs.edit()
                for ((key, value) in map) {
                    set(prefs, value, key)
                }
                editor.commit()
                return
            }
            val strings = any as? List<String>
            if (strings != null) {
                val editor = prefs.edit()
                editor.putStringSet(forKey, strings.toSet())
                editor.commit()
                return
            }
            val bool = any as? Boolean
            if (bool != null) {
                set(bool, forKey)
                return
            }
            val int = any as? Int
            if (int != null) {
                set(int, forKey)
                return
            }
            val float = any as? Float
            if (float != null) {
                set(float, forKey)
                return
            }
            val double = any as? Double
            if (double != null) {
                set(double, forKey)
                return
            }
        } else {
            val editor = prefs.edit()
            editor.remove(forKey)
            editor.commit()
        }
    }

}