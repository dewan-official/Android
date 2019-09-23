package com.dewansoft.app.chatapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RegisterOrLogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_login);
        // Set Fragment Login
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.register_or_login_framelayout_id,new Login()).commit();
    }
}
