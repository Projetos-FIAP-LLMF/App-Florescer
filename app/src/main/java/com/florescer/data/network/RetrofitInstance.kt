package com.florescer.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: HumorApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://florescer-mock-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HumorApi::class.java)
    }
}
