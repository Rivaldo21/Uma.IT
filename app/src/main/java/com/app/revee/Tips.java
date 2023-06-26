package com.app.revee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tips extends AppCompatActivity {

    ListView listview;
    WebView webView;
    ProgressBar progress;
    SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        listview = findViewById(R.id.list_view);

//      getSupportActionBar().hide();
        getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tips_toolbar);

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

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final SwipeRefreshLayout refreshLayout = findViewById(R.id.swipeRefreshlayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        webView.loadUrl("https://blog.rivaldojoseguterres.site/rj/tips-tricks/");
                    }
                }, 300);
            }
        });

        overridePendingTransition(0, 0);
//        Konfigura WebView iha ne...
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://blog.rivaldojoseguterres.site/rj/tips-tricks/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebViewClient(new MyWebViewClient());

        progress = findViewById(R.id.progress);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(Tips.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_edukasaun:
                        Intent intent2 = new Intent(Tips.this, Favoritu.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_servisu:
                        break;

                    case R.id.ic_sertifikadu:
                        Intent intent4 = new Intent(Tips.this, Login.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        listView = findViewById(R.id.list_view);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Halo pagina website bele load no scroll...
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listView.getChildAt(0) != null) {
                    if (listView.getFirstVisiblePosition() > 0 || listView.getChildAt(0).getTop() < 0) {
                        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight());
                    } else {
                        bottomNavigationView.animate().translationY(0);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // Klase WebViewClient hodi bele loke URL iha activity foun iha android
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // Loke URL iha activity foun
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtra("url", request.getUrl().toString());
            startActivity(intent);
            return true;
        }

        ProgressBar progressBar = findViewById(R.id.progressBar);

        private class MyWebViewClients extends WebViewClient {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE); // Hamosu ProgressBar bainhira load pagina
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE); // Halakon ProgressBar depois de pagina load ona
            }

            // Metode seluk bele aumenta...
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}