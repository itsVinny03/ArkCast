package com.example.arkcast

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("V4/Others/Kurt/LatestVersionAPK/ArkCast/output-metadata.json")
    fun getAppUpdateDetails(): Call<AppUpdateResponse>
}
