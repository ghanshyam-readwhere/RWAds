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
        linearLayout.removeAllViews()
        adView = AdView(this)
        linearLayout.addView(adView)
        val request = AdRequest.Builder().build()
        adView.setAdSize(AdSize.LARGE_BANNER)
        adView.adUnitId ="ca-app-pub-3940256099942544/6300978111"
        adView.updateAdsWithRwFlow(linearLayout)
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

