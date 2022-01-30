//package com.example.duos.data.remote.PartnersPage
//
//import android.util.Log
//import com.google.gson.JsonElement
//import retrofit2.Call
//import retrofit2.Response
//
//// 레트로핏 인터페이스와 레트로핏
//class PartnerPageRetrofitManager {
//    val TAG : String = "PartnerPage"
//
//    companion object{
//
//        val instance = PartnerPageRetrofitManager()
//    }
//
//    val httpCall : PartnerPageRetrofitInterface? = PartnerPageRetrofitClient.getClient("https://duos.co.kr/")?.create(PartnerPageRetrofitInterface::class.java)
//
//    fun goPartnerPage(){
//
//        var call = httpCall?.goPartnerPage()
//
//        call?.enqueue(object : retrofit2.Callback<JsonElement>{
//            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
//
//                Log.d(TAG,"RetrofitManager - getTodo() - onResponse() called / response: ${response}")
//                Log.d(TAG,"RetrofitManager - getTodo() - onResponse() called / response: ${response.body()}")
//            }
//
//            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
//
//
//                Log.d(TAG,"RetrofitManager - getTodo() - onFailure() called / t: ${t}")
//            }
//
//        })
//
//    }
//
//}