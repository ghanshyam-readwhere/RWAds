package com.rwadswhitelabel.network

import com.google.gson.JsonObject
import com.rwadswhitelabel.models.ConfigResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiInterface {

    @GET
    fun getAppConfig(@Url  id :String) : Call<ConfigResponses>
}