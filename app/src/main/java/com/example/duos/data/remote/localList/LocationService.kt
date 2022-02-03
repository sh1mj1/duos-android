package com.example.duos.data.remote.localList

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ui.signup.localSearch.LocationView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LocationService {
    val retrofit = NetworkModule.getRetrofit()
    fun getLocationList(locationView: LocationView) {
        val locationListService = retrofit.create(LocationRetrofitInterface::class.java)

        locationListService.locationList().enqueue(object :
            Callback<LocationResponse> {

            override fun onResponse(
                call: Call<LocationResponse>,
                response: Response<LocationResponse>
            ) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 ->{
                        val locationCategoryList = resp.result.locationCategoryList
                        val locationList = resp.result.locationList
                        locationView.onLocationCategoryListOnView(locationCategoryList)
                        locationView.onLocationListOnView(locationList)
                    }
                    else -> locationView.onLocationFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                locationView.onLocationFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}