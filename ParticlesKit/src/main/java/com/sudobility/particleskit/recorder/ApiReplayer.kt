package com.sudobility.particleskit.recorder

import com.sudobility.particleskit.services.folder.FolderService
import com.sudobility.utilities.extensions.joined
import com.sudobility.utilities.extensions.sorted
import com.sudobility.utilities.extensions.stringByAppendingPathComponent
import com.sudobility.utilities.files.Directory
import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.json.JsonWriter
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.Debouncer
import com.sudobility.utilities.utils.debug.DebugSettings
import java.io.File

public enum class ApiReplayMode {
    normal,
    recording,
    replay
}

public interface ApiReplayerProtocol {
    val mode: ApiReplayMode
    fun replay(urlPath: String, params: List<String>?) : Any?
    fun record(urlPath: String, params: List<String>?, data: Any?)
}

public class ApiReplayer {
    companion object {
        var shared: ApiReplayerProtocol? = null
    }
}

public class JsonApiReplayer: NSObject, ApiReplayerProtocol {
    var path: File? = null
    var key: String? = "default"
    var recording: MutableMap<String, Any>? = null
    var debouncer: Debouncer = Debouncer()
    override val mode: ApiReplayMode
        get() {
            val testString = parser.asString(DebugSettings.debug?.get("integration_test"))
            if (testString != null) {
                if (testString == "r") {
                    return ApiReplayMode.recording
                } else if (testString == "t") {
                    return ApiReplayMode.replay
                }
            }
            val modeString = parser.asString(DebugSettings.debug?.get("api_replay"))
            if (modeString != null) {
                if (modeString == "r") {
                    return ApiReplayMode.recording
                } else if (modeString == "p") {
                    return ApiReplayMode.replay
                }
            }
            return ApiReplayMode.normal
        }
    val persistTag: String?
        get() {
            val key = key
            if (key != null) {
                return "${className}.persist.${key}"
            }
            return null
        }
    val persistDataFile: String?
        get() {
            val persistTag = persistTag
            if (persistTag != null) {
                val path = path ?: FolderService.shared?.documents()
                if (path != null) {
                    Directory.ensure(path)
                    return path.path.stringByAppendingPathComponent(path = "${persistTag}.data.json")
                }
            }
            return null
        }

    constructor(path: File? = null) : super() {
        this.path = path
        load()
    }

    private fun uniqueUrl(urlPath: String, params: List<String>?) : String {

        val params = params?.sorted({ string1, string2  ->
            if (string1 < string2) {
                 1
            } else {
                 -1
            }
        })
        var urlPath = urlPath
        if (params != null) {
            if (params.isNotEmpty()) {
                urlPath = urlPath + "?" + params.joined(separator = "&")
            }
        }
        return urlPath
    }

    override fun replay(urlPath: String, params: List<String>?) : Any? {
        if (mode == ApiReplayMode.replay) {
            val url = uniqueUrl(urlPath = urlPath, params = params)
            return recording?.get(url)
        }
        return null
    }

    override fun record(urlPath: String, params: List<String>?, data: Any?) {
        if (mode == ApiReplayMode.replay || mode == ApiReplayMode.recording) {
            if (recording == null) {
                recording = mutableMapOf()
            }
            val url = uniqueUrl(urlPath = urlPath, params = params)
            if (data != null) {
                recording?.set(url, data)
            } else {
                recording?.remove(url)
            }
            val handler = debouncer.debounce()
            if (handler != null) {
                handler.run({
                    this.save()
                }, delay = 0.5F)
            }
        }
    }

    private fun save() {
        val persistDataFile = this.persistDataFile
        val persist = recording
        if (persistDataFile != null && persist != null) {
            JsonWriter.write(persist, to = persistDataFile)
        }
    }

    open fun load() {
        val persistDataFile = this.persistDataFile
        val recording = JsonLoader.load(file = persistDataFile) as? MutableMap<String, Any>
        if (persistDataFile != null && recording != null) {
            this.recording = recording
        }
    }
}
