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
import android.widget.Toast;

public class GameOverActivity extends AppCompatActivity {

    private TextView scoreInfoTV;

    DataBaseHelper dataBaseHelper;

    ArrayAdapter playerArrayAdapter;

    ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getSupportActionBar().setTitle("Snake Game Information");

        scoreInfoTV = findViewById(R.id.scoreInfoTV);

        scoreInfoTV.setText("Your score is " + String.valueOf(getIntent().getIntExtra("score", 0 )));

        mainListView = findViewById(R.id.mainListView);

        // adaugam player actual si afisam lista
        PlayerModel playerModel = null;

        try{
            playerModel = new PlayerModel(-1, getIntent().getIntExtra("score", 0 ));
        }
        catch(Exception exception){
            playerModel = new PlayerModel(-1,0);
        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(GameOverActivity.this);
        boolean actionSuccess = dataBaseHelper.addOne(playerModel);

        //actualizare continut din lista
        dataBaseHelper = new DataBaseHelper(GameOverActivity.this);
        playerArrayAdapter = new ArrayAdapter<PlayerModel>(GameOverActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone());
        mainListView.setAdapter(playerArrayAdapter);

        final Button rankBtn = findViewById(R.id.rankBtn);
        rankBtn.setBackgroundColor(Color.YELLOW);
        rankBtn.setTextColor(Color.BLACK);

        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bestPlayer = new Intent(GameOverActivity.this, BestPlayerActivity.class);
                startActivity(bestPlayer);
            }
        });


        final Button newGameBtn = findViewById(R.id.newGameBtn);
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
}