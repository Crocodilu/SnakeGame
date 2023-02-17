package com.example.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private Button startGameBtn;
    private EditText usernameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Snake Game - Register");

        startGameBtn = findViewById(R.id.startGameBtn);
        usernameET = findViewById(R.id.usernameET);

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGame = new Intent(RegisterActivity.this, MainActivity.class);
                startGame.putExtra("username", usernameET.getText().toString());
                startActivity(startGame);

                // finish RegisterActivity activity
                finish();
            }
        });

    }
}