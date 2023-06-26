package com.app.revee;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Favoritu extends AppCompatActivity {

    ListView listview;
    WebView webView;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritu);
        listview=findViewById(R.id.list_view2);

        overridePendingTransition(0, 0);

        getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.favorite_toolbar);

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

//        Konfigura WebView iha ne...
        webView=findViewById(R.id.webview2);
        webView.loadUrl("");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked( true );

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(Favoritu.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_edukasaun:
                        break;

                    case R.id.ic_servisu:
                        Intent intent3 = new Intent(Favoritu.this, Tips.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_sertifikadu:
                        Intent intent4 = new Intent(Favoritu.this, Login.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        listView = findViewById(R.id.list_view2);

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
}