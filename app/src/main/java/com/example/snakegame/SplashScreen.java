package com.example.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // hide action bar
        getSupportActionBar().hide();

        // creates a new instance of the Handler class and then uses the postDelayed method
        // to schedule the execution of a Runnable after a delay of 2500 milliseconds (2.5 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent registerUser = new Intent(SplashScreen.this, RegisterActivity.class);
                startActivity(registerUser);
                // finish SplashScreen activity
                finish();
            }
        }, 2500);

    }
}