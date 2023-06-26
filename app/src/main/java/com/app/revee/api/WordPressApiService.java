package com.app.revee.api;

import com.app.revee.model.TokenResponse;
import com.app.revee.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public interface WordPressApiService {
    @FormUrlEncoded
    @POST("?rest_route=/simple-jwt-login/v1/users") // Ganti dengan endpoint register di situs WordPress Anda
    Call<ResponseBody> registerUser(@Field("email") String email, @Field("password") String password, @Field("username") String username, @Field("Rikudev1234") String Rikudev1234);

    @FormUrlEncoded
    @POST("?rest_route=/simple-jwt-login/v1/auth")
    Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password, @Field("Rikudev1234") String Rikudev1234);
}