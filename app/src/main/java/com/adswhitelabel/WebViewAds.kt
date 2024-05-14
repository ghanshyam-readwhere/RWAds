package com.adswhitelabel

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class WebViewAds : AppCompatActivity() {

    private lateinit var  webView : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView= findViewById(R.id.webView)

        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true

        loadAdIntoWebView()
    }

    private fun loadAdIntoWebView() {
        // Load the ad content
        val adHtml =
            "<html><script async src=\"https://securepubads.g.doubleclick.net/tag/js/gpt.js\"></script>" +
                    "<script>" +
                    "  window.googletag = window.googletag || {cmd: []};" +
                    "  googletag.cmd.push(function() {" +
                    "    googletag.defineSlot('/21833905170,23071623886/Anynews_300x600', [300, 600], 'div-gpt-ad-1713864992569-0').addService(googletag.pubads());" +
                    "    googletag.pubads().enableSingleRequest();" +
                    "    googletag.enableServices();" +
                    "  });" +
                    " <body> </script><!-- /21833905170,23071623886/Anynews_300x600 -->" +
                    "<div id='div-gpt-ad-1713864992569-0' style='min-width: 300px; min-height: 600px;'>" +
                    "  <script>" +
                    "    googletag.cmd.push(function() { googletag.display('div-gpt-ad-1713864992569-0'); });" +
                    "  </script>" +
                    "</div>" +
                    "                        </body></html>"
        webView.loadData(adHtml, "text/html", "UTF-8")

        // Optionally, handle URL requests within the WebView
        webView.setWebViewClient(WebViewClient())
    }
}