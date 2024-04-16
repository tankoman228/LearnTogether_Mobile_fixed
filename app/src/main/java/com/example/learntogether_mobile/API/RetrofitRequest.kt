package com.example.learntogether_mobile.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Класс для работы с запросами RETROFIT, через его объекты и идёт запрос
 */
class RetrofitRequest {
    @JvmField
    var apiService: ApiServiceInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Variables.server)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiServiceInterface::class.java)
    }
}
