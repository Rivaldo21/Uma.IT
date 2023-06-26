package com.app.revee;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    ImageView backWeb;
    ProgressBar progressBar;
    WebView webview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        WebView webview1 = findViewById(R.id.webview1);
        webview1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.webview_toolbar);

        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();
                boolean shouldChangeStatusBarTintToDark = true;
                getWindow().setStatusBarColor(Color.WHITE);
                if (shouldChangeStatusBarTintToDark) {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    // Ita hakarak atu troka kor tint ba mutin iha ne nafatin.
                    decor.setSystemUiVisibility(0);
                }
            }
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView backWeb = findViewById(R.id.backWeb);
        backWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Hetan URL husi Intent
        String url = getIntent().getStringExtra("url");

        // Mendapatkan referensi ke WebView
        webView = findViewById(R.id.webview1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }
}