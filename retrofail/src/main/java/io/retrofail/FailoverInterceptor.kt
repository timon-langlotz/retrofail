package io.retrofail

import android.net.Network
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.EOFException
import java.io.IOException
import java.net.BindException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class FailoverInterceptor(
    private val okHttpClient: OkHttpClient,
    private val networkProvider: NetworkProvider,
    private val logger: Logger,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return tryExecuteOnNetworks(
            chain.request(),
            networkProvider.getAvailableNetworks().iterator(),
            onNoNetworksLeftToTry = { IOException("No network interfaces available") }
        )
    }

    private fun tryExecuteOnNetworks(
        request: Request,
        networks: Iterator<Network>,
        onNoNetworksLeftToTry: () -> IOException,
    ): Response {
        if (!networks.hasNext()) {
            logger.d("No network interfaces left to try")
            throw onNoNetworksLeftToTry()
        }
        val network = networks.next()
        return try {
            logger.d("Executing call on network interface $network")
            executeOnNetwork(network, request)
        } catch (e: IOException) {
            if (attemptFailover(e)) {
                logger.d("Failed to execute call on network interface $network, " +
                        "attempting failover", e)
                tryExecuteOnNetworks(request, networks, onNoNetworksLeftToTry = { e })
            } else {
                logger.d("Failed to execute call on network interface $network, " +
                        "not attempting failover", e)
                throw e
            }
        }
    }

    private fun executeOnNetwork(network: Network, request: Request): Response {
        val client = okHttpClient.newBuilder()
            .socketFactory(network.socketFactory)
            .build()
        val call = client.newCall(request)
        return call.execute()
    }

    private fun attemptFailover(e: IOException): Boolean {
        return when (e) {
            is BindException,
            is ConnectException,
            is EOFException,
            is SocketTimeoutException,
            is UnknownHostException -> true

            else -> false
        }
    }
}
