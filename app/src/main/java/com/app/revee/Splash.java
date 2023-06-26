package com.app.revee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SharedPreferences pref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                if (pref.getBoolean("firstStart", true)) {
                    startActivity(new Intent(Splash.this, Welcome1.class));
                } else {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                }
                finish();
            }
        }, secondsDelayed * 1000);
    }
}