package com.adswhitelabel

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.rwadswhitelabel.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdView: AppCompatButton
    private lateinit var btnAdManagerView: AppCompatButton
    private lateinit var btnInt: AppCompatButton
    private lateinit var btnAdMobInt: AppCompatButton
    private lateinit var btnNativeAds: AppCompatButton
    private var mAdManagerInterstitialAd: AdManagerInterstitialAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAdView = findViewById(R.id.btnAdView)
        btnAdManagerView = findViewById(R.id.btnAdManagerView)
        btnInt = findViewById(R.id.btnInt)
        btnAdMobInt = findViewById(R.id.btnAdMobInt)
        btnNativeAds = findViewById(R.id.btnNativeAds)
        val url =
            "https://d1mtg6picedtk1.cloudfront.net/mcms-apps-apk/Rw_Ads_Whitelabel/com.readwhere.whitelabel.punjabitribune.json"

        RwAdsIntialize.init(this,url)
        btnAdView.setOnClickListener {
            startActivity(Intent(this,AdViewActivity::class.java))
        }

        btnAdManagerView.setOnClickListener {
            startActivity(Intent(this,AdManagerActivity::class.java))
        }
        btnInt.setOnClickListener {
            startActivity(Intent(this,InterstitialActivity::class.java))
        }
        btnAdMobInt.setOnClickListener {
            mInterstitialAd!!.show(this@MainActivity);

//            startActivity(Intent(this,AdMobInterstitialActivity::class.java))
        }
        btnNativeAds.setOnClickListener {
            startActivity(Intent(this,NativeAdActivity::class.java))
        }
        Handler().postDelayed(Runnable {
            val adRequest: AdRequest = AdRequest.Builder().build()

            InterstitialAd.load(this, "/1009127/pt_story_swipe_int", adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        // The mAdManagerInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd
                        Log.d("TAG", "onAdLoaded")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString())
                        mInterstitialAd = null
                    }
                })
        },2000);


    }
}