package com.rwadswhitelabel.models

import com.google.gson.annotations.SerializedName

data class ConfigResponses (
    @SerializedName("status") val status: Boolean,
    @SerializedName("rw_overwrite") val rw_overwrite: Int = 0,
    @SerializedName("request_batch") val requestBatch: Int = 0,
    @SerializedName("auto_refresh") val autoRefresh: Int = 0,

    @SerializedName("data") val data: ArrayList<AdsMediationListing>,
)