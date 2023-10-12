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


        adManager = AdManagerAdView(this)
        linearLayout.removeAllViews()
        linearLayout.addView(adManager)
        val request = AdManagerAdRequest.Builder().build()
        adManager.setAdSizes(AdSize(320,50))
        adManager.adUnitId ="/1009127/Readwhere_App_Banner_android_home_top"
        adManager.updateAdsWithRwFlow(linearLayout)

        adManager.loadAd(request)

    }

}