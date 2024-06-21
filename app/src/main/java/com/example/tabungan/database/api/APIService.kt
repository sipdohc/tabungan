package com.example.tabungan.database.api

import android.app.VoiceInteractor
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @GET("user")
    fun getUser(): Call<List<Response>>

}
