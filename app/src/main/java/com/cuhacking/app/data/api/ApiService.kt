package com.cuhacking.app.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("hello/{id}")
    suspend fun exampleApiMethod(@Path("id") id: String): String
}