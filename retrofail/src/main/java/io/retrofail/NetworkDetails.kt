package io.retrofail

import android.net.LinkProperties
import android.net.NetworkCapabilities

/**
 * Contains detail information of a network.
 */
public interface NetworkDetails {
    /**
     * Get the [NetworkCapabilities] of a network.
     *
     * @return The [NetworkCapabilities].
     */
    public fun getNetworkCapabilities(): NetworkCapabilities

    /**
     * Get the [LinkProperties] of a network.
     *
     * @return The [LinkProperties].
     */
    public fun getLinkProperties(): LinkProperties
}
