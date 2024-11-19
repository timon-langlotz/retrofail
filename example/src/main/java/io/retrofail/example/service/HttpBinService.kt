package io.retrofail.example.service

import okhttp3.ResponseBody
import retrofit2.http.GET

interface HttpBinService {
    @GET("get")
    suspend fun get(): ResponseBody

    companion object {
        const val BASE_URL = "https://httpbin.org/"
    }
}
