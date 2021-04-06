package com.sudobility.utilities.utils.debug

import android.content.Context
import android.content.pm.PackageManager
import java.util.Arrays.asList


public object Installation {
    public var context: Context? = null

    public val appStore: Boolean
        get() {
            context?.let {
                return verifyInstallerId(context!!)
            }
            return false
        }

    private fun verifyInstallerId(context: Context): Boolean {
        // A list with valid installers package name
        val validInstallers = asList("com.android.vending", "com.google.android.feedback")

        // The package name of the app that has installed your app
        val installer = context?.getPackageManager().getInstallerPackageName(context.getPackageName())

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer)
    }
}
