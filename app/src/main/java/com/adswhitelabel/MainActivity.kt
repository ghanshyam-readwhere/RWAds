package com.adswhitelabel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdView: AppCompatButton
    private lateinit var btnAdManagerView: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdView = findViewById(R.id.btnAdView)
        btnAdManagerView = findViewById(R.id.btnAdManagerView)
        btnAdView.setOnClickListener {
            startActivity(Intent(this,AdViewActivity::class.java))
        }
        btnAdManagerView.setOnClickListener {
            startActivity(Intent(this,AdManagerActivity::class.java))
        }
    }
}