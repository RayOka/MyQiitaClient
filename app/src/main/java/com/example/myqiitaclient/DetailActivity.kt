package com.example.myqiitaclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val webView = findViewById<WebView>(R.id.web_view)
        val url = intent.getStringExtra("URL")

        // リンクをタップしたときに標準ブラウザを起動させない
        webView .webViewClient = WebViewClient()

        // 最初に投稿を表示
        url?.let { webView.loadUrl(it) }

        // javascriptを許可する
        webView.settings.javaScriptEnabled = true
    }
}