package com.example.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameOverActivity extends AppCompatActivity {

    TextView scoreInfoTV;

    // used to perform operations such as inserting, updating, and retrieving data from a database
    DataBaseHelper dataBaseHelper;

    // used to adapt arrays to ListViews
    ArrayAdapter playerArrayAdapter;

    ListView mainListView;

    Button bestBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getSupportActionBar().setTitle("Snake Game - Information");

        // getting scoreInfoTextView from xml file
        scoreInfoTV = findViewById(R.id.scoreInfoTV);

        // setting scoreInfoTextView with score received from MainActivity
        scoreInfoTV.setText("Your score is " + String.valueOf(getIntent().getIntExtra("score", 0 )));

        // getting mainListView from xml file
        mainListView = findViewById(R.id.mainListView);

        int score = getIntent().getIntExtra("score", 0 );
        String username = getIntent().getStringExtra("username");

        // if player has not provided a username, we put the default value "anonymous"
        if(username.equals("")){
            username = "anonymous";
        }

        if(score > 0) {
            // add the current player
            PlayerModel playerModel = null;
            try {
                playerModel = new PlayerModel(-1, score, username);
            } catch (Exception exception) {
                playerModel = new PlayerModel(-1, 0, null);
            }

            DataBaseHelper dataBaseHelper = new DataBaseHelper(GameOverActivity.this);
            boolean actionSuccess = dataBaseHelper.addOne(playerModel);
        }// if

        // update and display list content - in this case, all saved players
        dataBaseHelper = new DataBaseHelper(GameOverActivity.this);
        playerArrayAdapter = new ArrayAdapter<PlayerModel>(GameOverActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone());
        mainListView.setAdapter(playerArrayAdapter);

        // getting bestButton from xml file
        bestBtn = findViewById(R.id.bestBtn);
        bestBtn.setBackgroundColor(Color.YELLOW);
        bestBtn.setTextColor(Color.BLACK);

        bestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bestPlayer = new Intent(GameOverActivity.this, BestPlayerActivity.class);
                startActivity(bestPlayer);
            }
        });

        // getting newGameButton from xml file
        Button newGameBtn = findViewById(R.id.newGameBtn);
        newGameBtn.setBackgroundColor(Color.CYAN);
        newGameBtn.setTextColor(Color.BLACK);

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gameOver = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(gameOver);
                }
            });
    }//onCreate
}// GameOverActivity class