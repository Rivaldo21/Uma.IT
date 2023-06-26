package com.app.revee;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {


    TextView txtLag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        overridePendingTransition(0,0);

        TextView txtLag = findViewById(R.id.txtLag);
        txtLag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#1C4CF3")));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.register_toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }

        ImageView backButton = getSupportActionBar().getCustomView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil metode onBackPressed() untuk kembali ke activity sebelumnya
                onBackPressed();
            }
        });

//        {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                View decor = getWindow().getDecorView();
//                boolean shouldChangeStatusBarTintToDark = true;
//                getWindow().setStatusBarColor(Color.WHITE);
//                if (shouldChangeStatusBarTintToDark) {
//                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                } else {
//                    // Ita hakarak atu troka kor tint ba mutin iha ne nafatin.
//                    decor.setSystemUiVisibility(0);
//                }
//            }
//        }

//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView backWeb = findViewById(R.id.backWeb);
//        backWeb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//
//        // Hetan URL husi Intent
//        String url = getIntent().getStringExtra("url");
//
//        // Mendapatkan referensi ke WebView
//        webView = findViewById(R.id.webview6);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl(url);

    }
}