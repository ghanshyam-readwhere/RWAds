package com.adswhitelabel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.rwadswhitelabel.RwNativeAdView

class NativeAdActivity :AppCompatActivity() {

    private lateinit var adView : NativeAdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ads)
        adView = findViewById(R.id.ad_view)
        val id = "/6499/example/nativeee"
        val adLoader = AdLoader.Builder(this, id)
            .forNativeAd { ad : NativeAd ->
                adView.mediaView = adView.findViewById(R.id.contentad_image)
                adView.headlineView = adView.findViewById(R.id.contentad_headline)
                adView.bodyView = adView.findViewById(R.id.contentad_body)
                adView.iconView = adView.findViewById(R.id.contentad_logo)
                adView.advertiserView = adView.findViewById(R.id.contentad_advertiser)
                adView.starRatingView = adView.findViewById(R.id.ad_stars)
                adView.callToActionView = adView.findViewById(R.id.contentad_call_to_action)
                adView.setNativeAd(ad)

                // Show the ad.
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    RwNativeAdView.initNativeAds(this@NativeAdActivity,adView,id)
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build())
            .build()

        adLoader.loadAd(AdManagerAdRequest.Builder().build())

    }




}