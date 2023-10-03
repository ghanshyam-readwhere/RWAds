package com.adswhitelabel

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.rwadswhitelabel.AdManagerAdView

//import com.google.android.gms.ads.admanager.AdManagerAdView

class AdManagerActivity : AppCompatActivity() {

    private lateinit var adManager: AdManagerAdView
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_manager)

        linearLayout = findViewById(R.id.linearLayout)

        val list : ArrayList<String> = ArrayList()
        list.add("/6499/example/bannerq")
        list.add("/6499/example/bannerw")
        list.add("/6499/example/bannerr")
        list.add("/6499/example/banner")


        adManager = AdManagerAdView(this)
        linearLayout.removeAllViews()
        linearLayout.addView(adManager)
        val request = AdManagerAdRequest.Builder().build()
        adManager.adSize = AdSize.LARGE_BANNER
        adManager.adUnitId ="/6499/example/banner"
        adManager.updateAdsWithRwFlow(linearLayout,list)

        adManager.loadAd(request)

    }

}