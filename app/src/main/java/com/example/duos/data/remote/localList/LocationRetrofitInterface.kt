package com.example.duos.data.remote.localList

import retrofit2.Call
import retrofit2.http.GET

interface LocationRetrofitInterface {
    @GET("/test/location/list")
    fun locationList(): Call<LocationResponse>
}