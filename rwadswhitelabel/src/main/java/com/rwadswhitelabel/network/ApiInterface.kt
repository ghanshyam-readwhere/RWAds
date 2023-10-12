package com.rwadswhitelabel.network

import com.google.gson.JsonObject
import com.rwadswhitelabel.models.ConfigResponses
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET(Constants.GET_APP_CONFIG)
    fun getAppConfig() : Call<ConfigResponses>
}