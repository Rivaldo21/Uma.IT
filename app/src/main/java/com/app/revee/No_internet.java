package com.app.revee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.revee.ActivityNoInternetBinding;
import com.app.revee.MainActivity;
import com.app.revee.R;

public class No_internet extends AppCompatActivity {
    private ActivityNoInternetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        overridePendingTransition(0, 0);

        getSupportActionBar().hide();

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

        // Aumenta BroadcastReceiver hodi monitoriza mudansa husi koneksaun internet
        BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                boolean isInternetConnected = networkInfo != null && networkInfo.isConnected();

                if (isInternetConnected) {
                    // Koneksaun internet ativu, fila fali ba pagina varanda
                    Intent homeIntent = new Intent(No_internet.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish(); // Optional: finish the current activity to prevent going back to it
                } else {

                    WebView webView = findViewById(R.id.webview);
                    ImageView imageView = findViewById(R.id.imageView);
                    TextView textViewError = findViewById(R.id.textViewError);
                    // La iha koneksaun internet, hatudu layout fullscreen popup
                }
            }
        };

        // Registo BroadcastReceiver ba ACTION_CONNECTIVITY_CHANGE
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

    }
}