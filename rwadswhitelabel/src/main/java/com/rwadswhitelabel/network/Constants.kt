package com.rwadswhitelabel.network

import retrofit2.http.Path

interface Constants {

    companion object{
        const val BASE_URL = "https://d1mtg6picedtk1.cloudfront.net/mcms-apps-apk/Rw_Ads_Whitelabel/"
        const val GET_APP_CONFIG = "{packageName}.json"
    }
}