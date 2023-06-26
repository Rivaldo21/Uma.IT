package com.app.revee;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase;

    RecyclerView recyclerView;
    TextView txtUsername;
    Button btn1;
    RecyclerView.Adapter recycleViewAdapter;
    RecyclerView.LayoutManager recycleViewLayoutManager;
    ArrayList<ItemModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().hide();
        overridePendingTransition(0, 0);

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

        txtUsername = findViewById(R.id.username);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        final TextView txtView = (TextView) findViewById(R.id.tvView);

        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

            }
        });

        recycleViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recycleViewLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        data = new ArrayList<>();
        for (int i = 0; i <MyItem.Headline.length; i++) {
            data.add(new ItemModel(MyItem.Headline[i],
                    MyItem.Subheadline[i],
                    MyItem.iconlist[i]));
        }

        recycleViewAdapter = new MyRecycleView(data);
        recyclerView.setAdapter(recycleViewAdapter);

        if(mAuth.getCurrentUser() != null){
            getData(mAuth.getCurrentUser().getUid());
        }
    }

    void logout(){
        mAuth.signOut(); // Logout dari Firebase Authentication

        // Logout dari Google Sign-In
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Berhasil logout dari Google
                            Toast.makeText(Profile.this, "Sai husi perfil", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(Profile.this,Login.class);
                            startActivityForResult(myIntent, 0);
                        } else {
                            // Gagal logout dari Google
                            Log.w("logout", "Google sign out failed", task.getException());
                        }
                    }
                });
    }

    void getData(String key) {
        mDatabase.child("users").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(Profile.this, "Error getting data " + task.getException(), Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("uid", "get data " + task.getResult().child("name").getValue());
                    txtUsername.setText(String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });
    }
}