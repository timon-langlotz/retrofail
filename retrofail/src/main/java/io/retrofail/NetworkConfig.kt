package io.retrofail

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension

/**
 * An object describing a network configuration in terms of interface priorities.
 */
public class NetworkConfig internal constructor(
    internal val networkRequests: List<NetworkRequest>
) {
    /**
     * Builder used to create [NetworkConfig] objects.
     */
    public class Builder {
        private val networkRequests = mutableListOf<NetworkRequest>()

        /**
         * Adds a cellular network as the next priority.
         *
         * @param request The cellular [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        public fun addCellular(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_CELLULAR)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_CELLULAR)
        }

        /**
         * Adds a WiFi network as the next priority.
         *
         * @param request The WiFi [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        public fun addWifi(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_WIFI)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_WIFI)
        }

        /**
         * Adds a Bluetooth network as the next priority.
         *
         * @param request The Bluetooth [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        public fun addBluetooth(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_BLUETOOTH)
        }

        /**
         * Adds an ethernet network as the next priority.
         *
         * @param request The ethernet [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        public fun addEthernet(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_ETHERNET)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_ETHERNET)
        }

        /**
         * Adds a VPN network as the next priority.
         *
         * @param request The VPN [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        public fun addVpn(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_VPN)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_VPN)
        }

        /**
         * Adds a WiFi Aware network as the next priority.
         *
         * @param request The WiFi Aware [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        public fun addWifiAware(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_WIFI_AWARE)
        }

        /**
         * Adds a LoWPAN network as the next priority.
         *
         * @param request The LoWPAN [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        @RequiresApi(Build.VERSION_CODES.O_MR1)
        public fun addLowpan(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_LOWPAN)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_LOWPAN)
        }

        /**
         * Adds a USB network as the next priority.
         *
         * @param request The USB [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        @RequiresApi(Build.VERSION_CODES.S)
        public fun addUsb(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_USB)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_USB)
        }

        /**
         * Adds a Thread network as the next priority.
         *
         * @param request The Thread [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        @RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 7)
        public fun addThread(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_THREAD)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_THREAD)
        }

        /**
         * Adds a Satellite network as the next priority.
         *
         * @param request The Satellite [NetworkRequest].
         * @throws IllegalArgumentException If the transport is invalid.
         * @return The [Builder] object.
         */
        @RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 12)
        public fun addSatellite(
            request: NetworkRequest =
                createRequest(NetworkCapabilities.TRANSPORT_SATELLITE)
        ): Builder {
            return add(request, NetworkCapabilities.TRANSPORT_SATELLITE)
        }

        private fun createRequest(transport: Int): NetworkRequest {
            return NetworkRequest.Builder()
                .addTransportType(transport)
                .build()
        }

        private fun add(request: NetworkRequest, transport: Int): Builder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Validate that the transport is correctly set.
                require(request.hasTransport(transport)) { "Unsupported transport: $transport" }
            }
            networkRequests.add(request)
            return this
        }

        public fun build(): NetworkConfig {
            require(networkRequests.isNotEmpty()) { "No networks specified" }
            return NetworkConfig(networkRequests)
        }
    }
}
