package io.retrofail

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

internal class NetworkProvider(
    connectivityManager: ConnectivityManager,
    networkConfig: NetworkConfig,
    private val logger: Logger,
) {
    private val networks = LinkedHashMap<NetworkRequest, Network?>()

    init {
        for (networkRequest in networkConfig.networkRequests) {
            networks[networkRequest] = null
            val networkCallback = createNetworkCallback(networkRequest)
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    fun getAvailableNetworks(): List<Network> {
        return networks.values.filterNotNull()
    }

    private fun createNetworkCallback(request: NetworkRequest): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networks[request] = network
                logger.d("Network $network is available, request: $request")
            }

            override fun onLost(network: Network) {
                networks[request] = null
                logger.d("Network $network is lost, request: $request")
            }
        }
    }
}
