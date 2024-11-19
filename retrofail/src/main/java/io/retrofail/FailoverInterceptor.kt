package io.retrofail

import android.net.Network
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

internal class FailoverInterceptor(
    private val okHttpClient: OkHttpClient,
    private val networkProvider: NetworkProvider,
    private val logger: Logger,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return tryExecuteOnNetworks(
            chain.request(),
            networkProvider.getAvailableNetworks(),
            lastException = null
        )
    }

    private fun tryExecuteOnNetworks(
        request: Request,
        networks: List<Network>,
        lastException: IOException?,
    ): Response {
        if (networks.isEmpty()) {
            logger.d("No network interfaces left to try")
            throw lastException ?: IOException("No network interfaces available")
        }
        val network = networks.first()
        return try {
            logger.d("Executing call on network interface $network")
            executeOnNetwork(network, request)
        } catch (e: IOException) {
            logger.d("Failed to execute call on network interface $network", e)
            val remaining = networks.minus(network)
            tryExecuteOnNetworks(request, remaining, e)
        }
    }

    private fun executeOnNetwork(network: Network, request: Request): Response {
        val client = okHttpClient.newBuilder()
            .socketFactory(network.socketFactory)
            .build()
        val call = client.newCall(request)
        return call.execute()
    }
}
