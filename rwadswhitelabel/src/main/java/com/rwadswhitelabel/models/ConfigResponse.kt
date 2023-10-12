package com.rwadswhitelabel.models

import com.google.gson.annotations.SerializedName

data class ConfigResponses (
    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: ArrayList<AdsMediationListing>,
)