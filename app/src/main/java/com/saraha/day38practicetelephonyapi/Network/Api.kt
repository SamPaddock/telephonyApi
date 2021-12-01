package com.saraha.day38practicetelephonyapi.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {

    companion object{
        val baseURL = "https://618ebc2e50e24d0017ce141f.mockapi.io/"

        private val retrofit: Retrofit
        init {
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()).baseUrl(baseURL).build()
        }

        fun getInstance() = retrofit.create(ContactService::class.java)
    }
}