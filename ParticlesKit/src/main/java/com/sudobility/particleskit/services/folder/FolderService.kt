package com.sudobility.particleskit.services.folder

import com.sudobility.utilities.files.Directory
import java.io.File


public interface FolderProviderProtocol {
    fun documents() : File?
    fun temp() : File?
}

public class FolderService {
    companion object {
        var shared: FolderProviderProtocol? = null
    }
}

public class RealFolderProvider: FolderProviderProtocol {
    companion object {
        fun mock() : RealFolderProvider {
            val provider = RealFolderProvider()
            provider.documentFolder = null // ProcessInfo.processInfo.environment["TEST_DOCUMENTS_DIR"]
            provider.tempFolder = null // ProcessInfo.processInfo.environment["TEST_TEMP_DIR"]
            return provider
        }
    }

    public var documentFolder: File? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            Directory.ensure(documentFolder)
        }

    public var tempFolder: File? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            Directory.ensure(tempFolder)
        }

    public override fun documents() : File? =
        documentFolder ?: Directory.document

    public override fun temp() : File? =
        tempFolder ?: Directory.cache
}
