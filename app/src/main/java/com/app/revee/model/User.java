package com.app.revee.model;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public String password;
    public String Rikudev1234;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.Rikudev1234 = "Rikudev1234";
    }
}
