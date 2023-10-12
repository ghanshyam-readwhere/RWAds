package com.adswhitelabel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
//import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
//import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.rwadswhitelabel.AdManagerInterstitialAd
import com.rwadswhitelabel.AdManagerInterstitialAdLoadCallback


class InterstitialActivity : AppCompatActivity() {

    private var mAdManagerInterstitialAd: AdManagerInterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val adRequest: AdManagerAdRequest = AdManagerAdRequest.Builder().build()

        AdManagerInterstitialAd.load(this, "/6499/example/interstitial", adRequest,
            object : AdManagerInterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                    // The mAdManagerInterstitialAd reference will be null until
                    // an ad is loaded.
                    mAdManagerInterstitialAd = interstitialAd
                    mAdManagerInterstitialAd!!.show(this@InterstitialActivity);
                    Log.d("TAG", "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.d("TAG", loadAdError.toString())
                    mAdManagerInterstitialAd = null
                }
            })
    }
}