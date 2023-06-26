package com.app.revee;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.app.revee.api.ApiClient;
import com.app.revee.api.WordPressApiService;
import com.app.revee.model.TokenResponse;
import com.app.revee.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    String idToken;
    private WordPressApiService apiService;

    private int RC_SIGN_IN;

    private BottomNavigationView bottomNavigationView;
    TextView txtRejistu;
    private Button buttonLoginGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Konfigurasi login dengan Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Inisialisasi GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        apiService = ApiClient.getClient().create(WordPressApiService.class);

        buttonLoginGoogle = findViewById(R.id.btnGoogle);
        buttonLoginGoogle.setOnClickListener((View v)->{
            signInWithGoogle();
        });

        overridePendingTransition(0, 0);

        TextView txtFpassword = findViewById(R.id.txtFpassword);
        txtFpassword.setPaintFlags(txtFpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView txtRejistu = findViewById(R.id.txtRejistu);
        txtRejistu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#1C4CF3")));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.login_toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }

        ImageView backButton = getSupportActionBar().getCustomView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setVisibility(View.GONE);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(Login.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_edukasaun:
                        Intent intent2 = new Intent(Login.this, Favoritu.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_servisu:
                        Intent intent3 = new Intent(Login.this, Tips.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_sertifikadu:
                        break;
                }
                return false;
            }
        });
    }

    // Metode untuk memulai login dengan Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Metode untuk menangani hasil dari aktivitas login dengan Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    // Metode untuk menangani hasil login dengan Google
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String displayName = account.getDisplayName();
            String email = account.getEmail();
            String googleIdToken = account.getIdToken();
            String idToken = account.getIdToken();
            //String profilePictureUrl = account.getPhotoUrl().toString();
            Log.e("login data", displayName+" "+email+" "+account.getServerAuthCode()+" "+account.getId()+" "+googleIdToken);

            String username = account.getDisplayName();
            String password = account.getId();

            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);

        } catch (ApiException e) {
            // Penanganan jika
            // ogin dengan Google gagal
            Toast.makeText(this, "Login with Google failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential){

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("login", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            checkDataExistence(Objects.requireNonNull(task.getResult().getUser()));
                        }else{
                            Log.w("login", "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    void registerUser(User user, String id){
        Log.e("register", user.password);
        Call<ResponseBody> call = apiService.registerUser(user.email, user.password, user.username, user.Rikudev1234);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Registrasi berhasil
                    Toast.makeText(Login.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    mDatabase.child("users").child(id).setValue(user);
                    loginUser(user,id);
                    // Lakukan tindakan sesuai kebutuhan Anda (misalnya, navigasi ke halaman beranda)
                } else {
                    // Registrasi gagal
                    Toast.makeText(Login.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Error dalam koneksi atau permintaan API
                Toast.makeText(Login.this, "Registration error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(User user, String id) {
        Log.e("login", user.password);
        Call<ResponseBody> call = apiService.loginUser(user.email,user.password, user.Rikudev1234);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Registrasi berhasil
                    Toast.makeText(Login.this, "Login wordpress successful", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject data = jsonObject.getJSONObject("data");
                        String jwt = data.getString("jwt");
                        gotoProfile(jwt, id);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Lakukan tindakan sesuai kebutuhan Anda (misalnya, navigasi ke halaman beranda)
                } else {
                    // Registrasi gagal
                    Toast.makeText(Login.this, "Login wordpress failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Error dalam koneksi atau permintaan API
                Toast.makeText(Login.this, "Login wordpress  error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    void gotoProfile(String jwt,String id){
        mDatabase.child("users").child(id).child("JWT").setValue(jwt);
        Intent i = new Intent(Login.this, Profile.class);
        startActivity(i);
    }

    private void checkDataExistence(FirebaseUser firebaseUser) {
        // Referensi ke data yang ingin Anda periksa keberadaannya
        DatabaseReference dataRef = mDatabase.child("users").child(firebaseUser.getUid());


        // Tambahkan listener untuk mendapatkan data dari referensi
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Cek apakah data ada
                if (dataSnapshot.exists()) {
                    // Data ada
                    // Lakukan sesuatu di sini

                    User user = new User(String.valueOf(dataSnapshot.child("username").getValue()), String.valueOf(dataSnapshot.child("email").getValue()),String.valueOf(dataSnapshot.child("password").getValue()));
                    loginUser(user, firebaseUser.getUid());
                    Log.d("TAG", "Data exists!");
                } else {
                    // Data tidak ada
                    // Lakukan sesuatu di sini
                    String email = firebaseUser.getEmail();
                    String username = email;
                    String pass = randPass();
                    if (email != null && email.contains("@")) {
                        username = email.split("@")[0];
                    }
                    User user = new User(username, email,pass);
                   registerUser(user, firebaseUser.getUid());
                    Log.e("register", "Data doesn't exist! "+pass);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Jika terjadi error
                Log.e("TAG", "Failed to read data", databaseError.toException());
            }
        });
    }


    String randPass(){
        int length = 8; // Panjang string acak yang diinginkan
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        String randomString = sb.toString()+"12";
        return  randomString;
    }
}
