package com.sudobility.utilities.files

import com.sudobility.utilities.extensions.append
import com.sudobility.utilities.extensions.extension
import com.sudobility.utilities.extensions.lowercased
import com.sudobility.utilities.kvo.NSObject
import java.io.File


public object Directory: NSObject() {
    public var user: File? = null
    public var document: File? = DirectoryUtils.document
    public var library: File? =  null
    public var cache: File? = DirectoryUtils.cache
    public var bundle: File? = null

    public fun download() : File? {
        return  null
    }

    public fun temp() : String? {
        return  null
        }

    public fun folder(path: String?, folder: File?) : File? {
        return  null
        }

    public fun userFolder(path: String?) : File? {
        return folder(path, user)
    }


    public fun documentFolder(path: String?) : File? {
        return folder(path, document)
    }

    public fun libraryFolder(path: String?) : File? {
        return folder(path, library)
    }

    public fun downloadFolder(path: String?) : File? {
        return folder(path, download())
    }

    public fun cacheFolder(path: String?) : File? {
        return folder(path, cache)
    }

    public fun bundleFolder(path: String?) : File? {
        return folder(path, bundle)
    }

    public fun pictures() : File?{
        return folder("pictures", user)
    }

    public fun isFolder(path: File?) : Boolean {
        return path?.isDirectory ?: false
        }

    public fun exists(path: File?) : Boolean {
        return path?.exists() ?: false
    }

    public fun parent(path: File?) : File? {
        return path?.parentFile
    }

    public fun ensure(path: File?) : Boolean {
        if (exists(path)) {
            return true
        } else {
            val parent = parent(path)
            if (parent != null) {
                if (ensure(parent)) {
                    path?.mkdir()
                    return true
                } else {
                    return false
                }
            } else {
                return false
            }
        }
    }

    public fun delete(path: File?) {
        path?.deleteRecursively()
    }

    public fun folders(parent: File?) : List<File>? {
        val array = parent?.list()

        if (array != null) {
            var folders: MutableList<File> = mutableListOf()
            for (child: String? in array) {
                val fullPath = this.folder(child, parent)
                if (fullPath != null) {
                    if (isFolder(fullPath)) {
                        folders.append(fullPath)
                    }
                }
            }
            return folders
        }

        return null
    }

    public fun files(parent: File?, fileExtension: String?) : List<File>? {
        val array = parent?.list()

        if (array != null) {
            var files: MutableList<File> = mutableListOf()
            for (child: String in array) {
                val ext = child.extension?.lowercased()
                if (ext == fileExtension) {
                    val fullPath = this.folder(child, parent)
                    if (fullPath != null) {
                        if (!isFolder(fullPath)) {
                            files.append(fullPath)
                        }
                    }
                }
            }
            return files
        }

        return null
    }

}
