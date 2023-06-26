package com.app.revee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    WebView webView;
    SwipeRefreshLayout swipeRefresh;
    ProgressBar progressBar;
    volatile boolean isInternetConnected = false;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;
    private KeyEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.list_view);

        getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#1C4CF3")));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_home);

        getSupportActionBar().setElevation(0);

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.swipeRefresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        webView.loadUrl("https://blog.rivaldojoseguterres.site/rj/");
                    }
                }, 100);
            }
        });

        overridePendingTransition(0, 0);
//      Konfigura WebView iha ne...
        webView = findViewById(R.id.webview);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        WebView webView = findViewById(R.id.webview);
        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewError = findViewById(R.id.textViewError);

        boolean isInternetConnected = networkInfo != null && networkInfo.isConnected();

        if (isInternetConnected) {
            webView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setDomStorageEnabled(true);
            webView.loadUrl("https://blog.rivaldojoseguterres.site/rj/");
            webView.setWebViewClient(new MyWebViewClient());

        }

        // Aumenta BroadcastReceiver hodi haree mudansa koneksaun internet
        BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                boolean isInternetConnected = networkInfo != null && networkInfo.isConnected();

                if (isInternetConnected) {
                    webView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    webView.loadUrl("https://blog.rivaldojoseguterres.site/rj/");
                } else {
                    webView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    intent = new Intent(MainActivity.this, No_internet.class);
                    startActivity(intent);
                }
            }
        };

        // Aumenta BroadcastReceiver hodi ACTION_CONNECTIVITY_CHANGE
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        break;

                    case R.id.ic_edukasaun:
                        Intent intent2 = new Intent(MainActivity.this, Favoritu.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_servisu:
                        Intent intent3 = new Intent(MainActivity.this, Tips.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_sertifikadu:
                        Intent intent4 = new Intent(MainActivity.this, Profile.class);
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
                // Do nothing
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
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        private class MyWebViewClients extends WebViewClient {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (isInternetConnected) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (isInternetConnected) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            // Metode seluk...
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

