package com.app.revee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome1 extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);

        getSupportActionBar().hide();

        {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
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

        Button button = findViewById(R.id.btnNext1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = pref.edit();
                ed.putBoolean("firstStart", false);
                ed.apply();

                Intent intent = new Intent(Welcome1.this, Welcome2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}