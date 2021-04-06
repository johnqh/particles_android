package com.sudobility.utilities.files

import com.sudobility.utilities.app.UtilsApplication
import java.io.File

public object DirectoryUtils {
    public val document: File?
        get() {
            val app = UtilsApplication.shared()
            return app.filesDir
        }

    public val cache: File?
        get() {
            val app = UtilsApplication.shared()
            return app.cacheDir
        }

    public val external: File?
        get() {
            val app = UtilsApplication.shared()
            return app.getExternalFilesDir(null)
        }

}