package com.adswhitelabel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.rwadswhitelabel.RwAdsIntialize

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdView: AppCompatButton
    private lateinit var btnAdManagerView: AppCompatButton
    private lateinit var btnInt: AppCompatButton
    private lateinit var btnAdMobInt: AppCompatButton
    private lateinit var btnNativeAds: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAdView = findViewById(R.id.btnAdView)
        btnAdManagerView = findViewById(R.id.btnAdManagerView)
        btnInt = findViewById(R.id.btnInt)
        btnAdMobInt = findViewById(R.id.btnAdMobInt)
        btnNativeAds = findViewById(R.id.btnNativeAds)
        RwAdsIntialize.init(this,"1234567890")
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
            startActivity(Intent(this,AdMobInterstitialActivity::class.java))
        }
        btnNativeAds.setOnClickListener {
            startActivity(Intent(this,NativeAdActivity::class.java))
        }

    }
}