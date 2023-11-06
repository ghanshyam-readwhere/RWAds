package com.adswhitelabel

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.rwadswhitelabel.AdView

//import com.google.android.gms.ads.AdView

class AdViewActivity : AppCompatActivity() {

    private lateinit var adView: AdView
    private lateinit var linearLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adview)



        linearLayout = findViewById(R.id.linearLayout)

        adView = AdView(this)
        linearLayout.removeAllViews()
        linearLayout.addView(adView)
        val request = AdRequest.Builder().build()
        adView.setAdSize(AdSize.LARGE_BANNER)
        adView.adUnitId ="/1009127/Readwhere_App_Banner_android_home_bet_section"
//        adView.updateAdsWithRwFlow(linearLayout)
        adView.loadAd(request)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {

            }

            override fun onAdFailedToLoad(errorCode: LoadAdError) {

            }

            override fun onAdOpened() {

                // Save app state before going to the ad overlay.
            }

            override fun onAdClosed() {
            }
        }
        }
    }

