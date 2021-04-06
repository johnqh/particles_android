package com.sudobility.utilities.files

import java.io.File


public class FileUtils {
    companion object {
        public var shared: FileUtils = FileUtils()
    }

    public fun exists(directory: File?, file: String?) : Boolean {
        if (directory != null && file != null) {
            val file = File(directory!!, file!!)
            return file.exists()
        }
        return false
    }

    public fun delete(directory: File?, file: String?) {
        val file: File = File(directory, file)
        if (file.exists()) {
            file.delete()
        }
    }
}