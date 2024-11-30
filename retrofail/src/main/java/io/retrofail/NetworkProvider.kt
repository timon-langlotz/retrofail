package io.retrofail

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

internal class NetworkProvider(
    connectivityManager: ConnectivityManager,
    networkPriorityConfig: NetworkPriorityConfig,
    private val networkPriorityResolver: NetworkPriorityResolver?,
    private val logger: Logger,
) {
    private val requestedNetworkDetails = LinkedHashMap<NetworkRequest, NetworkWithDetails?>()

    init {
        for (networkRequest in networkPriorityConfig.networkRequests) {
            requestedNetworkDetails[networkRequest] = null
            val networkCallback = createNetworkCallback(networkRequest)
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    fun getAvailableNetworks(): List<Network> {
        return requestedNetworkDetails.values
            .filterNotNull()
            .filter { it.hasValidNetworkDetails() }
            .let {
                if (networkPriorityResolver != null) {
                    it.sortedWith { details1, details2 ->
                        networkPriorityResolver.compare(details1, details2)
                    }
                } else {
                    it
                }
            }
            .map { it.network }
    }

    private fun createNetworkCallback(request: NetworkRequest): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                logger.d("onAvailable: $network")
                requestedNetworkDetails[request] = NetworkWithDetails(network)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                logger.d("onCapabilitiesChanged: $network, $capabilities")
                requestedNetworkDetails.computeIfPresent(request) { _, details ->
                    details.copy(_capabilities = capabilities)
                }
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                logger.d("onLinkPropertiesChanged: $network, $linkProperties")
                requestedNetworkDetails.computeIfPresent(request) { _, details ->
                    details.copy(_linkProperties = linkProperties)
                }
            }

            override fun onLost(network: Network) {
                logger.d("onLost: $network")
                requestedNetworkDetails[request] = null
            }
        }
    }
}
