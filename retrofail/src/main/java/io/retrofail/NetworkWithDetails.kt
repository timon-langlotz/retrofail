package io.retrofail

import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities

internal data class NetworkWithDetails(
    val network: Network,
    val _capabilities: NetworkCapabilities? = null,
    val _linkProperties: LinkProperties? = null,
) : NetworkDetails {
    fun hasValidNetworkDetails(): Boolean {
        return _capabilities != null && _linkProperties != null
    }

    override fun getNetworkCapabilities(): NetworkCapabilities {
        return requireNotNull(_capabilities) { "Invalid NetworkDetails (NetworkCapabilities == null)" }
    }

    override fun getLinkProperties(): LinkProperties {
        return requireNotNull(_linkProperties) { "Invalid NetworkDetails (LinkProperties == null)" }
    }
}
