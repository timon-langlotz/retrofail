package io.retrofail.example

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.retrofail.NetworkPriorityConfig
import io.retrofail.Retrofail
import io.retrofail.example.service.HttpBinService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val service: HttpBinService

    init {
        val retrofail = Retrofail(application, LogcatLogger)
        val baseClient = OkHttpClient.Builder()
            .build()
        val networkPriorityConfig = NetworkPriorityConfig.createWifiThenCellular()
        service = Retrofit.Builder()
            .baseUrl(HttpBinService.BASE_URL)
            .client(retrofail.client(baseClient, networkPriorityConfig))
            .build()
            .create(HttpBinService::class.java)
    }

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.get()
                Log.d(TAG, "Response is ${response.string()}")
            } catch (e: IOException) {
                Log.d(TAG, "Failed", e)
            } catch (e: HttpException) {
                Log.d(TAG, "Failed", e)
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}