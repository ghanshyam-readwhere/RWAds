package com.adswhitelabel

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.*

class NativeAdActivity :AppCompatActivity() {

    private lateinit var adView : NativeAdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ads)
        adView = findViewById(R.id.ad_view)
//        adView.adUnitId ="/21833905170,23071623886/Anynews_APP_Banner_300x600"

        val id = "/21833905170,23071623886/Anynews_APP_Banner_300x600"
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
//            NativeCustomFormatAd
//            .forCustomFormatAd("12321712",
//            .forCustomFormatAd("10004520",
//                { ad ->
//                    val mediaContent: MediaContent? = ad.mediaContent
//
//                    // Assumes you have a FrameLayout in your view hierarchy with the id media_placeholder.
//
//                    // Assumes you have a FrameLayout in your view hierarchy with the id media_placeholder.
//                    val mediaPlaceholder = findViewById<View>(R.id.media_placeholder) as FrameLayout
//
//                    // Apps can check the MediaContent's hasVideoContent property to determine if the
//                    // NativeCustomFormatAd has a video asset.
//
//                    // Apps can check the MediaContent's hasVideoContent property to determine if the
//                    // NativeCustomFormatAd has a video asset.
//                    if (mediaContent != null && mediaContent.hasVideoContent()) {
//                        val mediaView = MediaView(mediaPlaceholder.context)
//                        mediaView.setMediaContent(mediaContent)
//                        mediaPlaceholder.addView(mediaView)
//
//                        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
//                        // VideoController will call methods on this object when events occur in the video
//                        // lifecycle.
//                        val vc: VideoController = mediaContent.getVideoController()
//
//                    } else {
//                        val mainImage = ImageView(this)
//                        mainImage.setAdjustViewBounds(true)
//                        mainImage.setImageDrawable(ad.getImage("Image")!!.drawable)
//
//                        val textView = TextView(this)
//                        textView.setText(ad.getText("Headline_Text"))
//                        textView.textSize = 34.0f
//                        mediaPlaceholder.addView(mainImage)
//                        mediaPlaceholder.addView(textView)
//
//                    }
//                },
//                { ad, s ->
//                    // Handle the click action
//                })
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("TAG", "onAdFailedToLoad: ")
                //    RwNativeAdView.initNativeAds(this@NativeAdActivity,adView,id)
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