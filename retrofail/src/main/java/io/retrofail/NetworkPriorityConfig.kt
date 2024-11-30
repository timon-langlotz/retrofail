package io.retrofail

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension

/**
 * An object describing a network configuration in terms of interface priorities. The order in which
 * network priorities are added represents the default order for network requests.
 */
public class NetworkPriorityConfig internal constructor(
    internal val networkRequests: List<NetworkRequest>
) {
    public companion object {
        /**
         * Creates a [NetworkPriorityConfig] that first uses WiFi, then cellular.
         *
         * @return The [NetworkPriorityConfig].
         */
        public fun createWifiThenCellular(): NetworkPriorityConfig {
            return Builder()
                .addWifi()
                .addCellular()
                .build()
        }

        /**
         * Creates a [NetworkPriorityConfig] that first uses ethernet, then WiFi, then cellular.
         *
         * @return The [NetworkPriorityConfig].
         */
        public fun createEthernetThenWifiThenCellular(): NetworkPriorityConfig {
            return Builder()
                .addEthernet()
                .addWifi()
                .addCellular()
                .build()
        }
    }

    /**
     * Builder used to create [NetworkPriorityConfig] objects.
     */
    public class Builder {
        private val networkRequests = mutableListOf<NetworkRequest>()

        /**
         * Adds a cellular network as the next priority.
         *
         * @return The [Builder] object.
         */
        public fun addCellular(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_CELLULAR)
        }

        /**
         * Adds a WiFi network as the next priority.
         *
         * @return The [Builder] object.
         */
        public fun addWifi(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_WIFI)
        }

        /**
         * Adds a Bluetooth network as the next priority.
         *
         * @return The [Builder] object.
         */
        public fun addBluetooth(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        }

        /**
         * Adds an ethernet network as the next priority.
         *
         * @return The [Builder] object.
         */
        public fun addEthernet(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_ETHERNET)
        }

        /**
         * Adds a VPN network as the next priority.
         *
         * @return The [Builder] object.
         */
        public fun addVpn(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_VPN)
        }

        /**
         * Adds a WiFi Aware network as the next priority.
         *
         * @return The [Builder] object.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        public fun addWifiAware(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
        }

        /**
         * Adds a LoWPAN network as the next priority.
         *
         * @return The [Builder] object.
         */
        @RequiresApi(Build.VERSION_CODES.O_MR1)
        public fun addLowpan(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_LOWPAN)
        }

        /**
         * Adds a USB network as the next priority.
         *
         * @return The [Builder] object.
         */
        @RequiresApi(Build.VERSION_CODES.S)
        public fun addUsb(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_USB)
        }

        /**
         * Adds a Thread network as the next priority.
         *
         * @return The [Builder] object.
         */
        @RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 7)
        public fun addThread(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_THREAD)
        }

        /**
         * Adds a Satellite network as the next priority.
         *
         * @return The [Builder] object.
         */
        @RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 12)
        public fun addSatellite(): Builder {
            return addRequest(NetworkCapabilities.TRANSPORT_SATELLITE)
        }

        private fun addRequest(transport: Int): Builder {
            val request = NetworkRequest.Builder()
                .addTransportType(transport)
                .build()
            networkRequests.add(request)
            return this
        }

        /**
         * Builds the [NetworkPriorityConfig].
         *
         * @throws [IllegalStateException] If no network priority has been specified.
         */
        public fun build(): NetworkPriorityConfig {
            check(networkRequests.isNotEmpty()) { "No networks specified" }
            return NetworkPriorityConfig(networkRequests)
        }
    }
}
