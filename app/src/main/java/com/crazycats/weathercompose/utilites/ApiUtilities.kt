package com.crazycats.weathercompose.utilites

import com.crazycats.weathercompose.ui.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    private var retrofit: Retrofit? = null

    fun getApiInterface(): ApiInterface? {
        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit?.create(ApiInterface::class.java)
    }
}