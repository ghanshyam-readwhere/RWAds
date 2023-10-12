package com.rwadswhitelabel.models

import com.google.gson.annotations.SerializedName

data class AdsMediationListing (
    @SerializedName("original_id") val originalId: String,
    @SerializedName("ad_server") val AdServer: String,
    @SerializedName("is_native") val isNative: Int,
    @SerializedName("switch_bet_native_to_banner") val switchBetNativeToBanner: Int,
    @SerializedName("mediations_adunits") val mediationsAdunits: ArrayList<String>,

)