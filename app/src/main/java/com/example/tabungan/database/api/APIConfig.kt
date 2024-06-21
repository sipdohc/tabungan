package com.example.tabungan.database.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIConfig {

    private const val BASE_URL = "https://6672f1cb6ca902ae11b27e05.mockapi.io/"

    fun getApiService(): APIService {
        // Membuat interceptor untuk logging HTTP. Level BODY berarti kita akan log detail request dan response.
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        // Membuat client HTTP dan menambahkan interceptor logging ke dalamnya
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        // Membuat instance Retrofit
        val retrofit = Retrofit.Builder()
            // Menentukan base URL untuk request API
            .baseUrl(BASE_URL)
            // Menambahkan converter factory untuk mengubah response menjadi objek Gson
            .addConverterFactory(GsonConverterFactory.create())
            // Menentukan client HTTP untuk Retrofit
            .client(client)
            .build()
        // Mengembalikan layanan API yang telah dibuat oleh Retrofit
        return retrofit.create(APIService::class.java)
    }
}
