package io.retrofail

import android.app.Application
import android.net.ConnectivityManager
import okhttp3.OkHttpClient

/**
 * Factory class for constructing an [OkHttpClient] with network interface failover capabilities.
 */
public class Retrofail(
    private val application: Application,
    private val logger: Logger = Logger { _, _, _, _ -> },
) {
    /**
     * Constructs a new [OkHttpClient] with network interface failover capabilities.
     *
     * @param baseClient The base [OkHttpClient] from which network specific clients will be derived.
     * @param networkConfig The [NetworkConfig].
     *
     * @return The [OkHttpClient] with network interface failover capabilities.
     */
    public fun client(baseClient: OkHttpClient, networkConfig: NetworkConfig): OkHttpClient {
        val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
        val networkProvider = NetworkProvider(connectivityManager, networkConfig, logger)
        val interceptor = FailoverInterceptor(baseClient, networkProvider, logger)
        return baseClient.newBuilder()
            .addInterceptor(interceptor)
            .build()
    }
}
