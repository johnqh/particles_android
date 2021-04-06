package com.sudobility.utilities.network

import android.content.Context
import android.net.*
import com.sudobility.utilities.app.UtilsApplication
import com.sudobility.utilities.kvo.KVOController
import com.sudobility.utilities.kvo.NSObject

public object NetworkConnection: NSObject() {
    override var _kvoController: KVOController? = null

    public var connected: Boolean?
        get() {
            return value("connected") as? Boolean
        }
        set(value) {
            set(value, "connected")
        }

    public var wifi: Boolean?
        get() {
            return value("wifi") as? Boolean
        }
        set(value) {
            set(value, "wifi")
        }

    private var connectivityManager: ConnectivityManager? = null
        set(value) {
            field = value
            val builder: NetworkRequest.Builder = NetworkRequest.Builder()

            connectivityManager?.registerNetworkCallback(
                builder.build(),
                object : ConnectivityManager.NetworkCallback() {

                    override fun onAvailable(network: Network) {
                        add(network)
                    }

                    override fun onLost(network: Network) {
                        remove(network)
                    }
                })
        }

    private var networks:MutableList<Network> = mutableListOf()

    init {
        checkStatus()
    }

    private fun checkStatus() {
        val app = UtilsApplication.shared()
        connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    private fun add(network: Network) {
        networks.add(network)
        connected = networks.count() != 0
        wifi = hasWifi()
    }

    private fun remove(network: Network) {
        networks.remove(network)
        connected = networks.count() != 0
        wifi = hasWifi()
    }

    private fun hasWifi(): Boolean {
        val wifi = networks.first { network ->
            val capabilities = connectivityManager?.getNetworkCapabilities(network) ?: return false
            return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
        }
        return wifi != null
    }
}