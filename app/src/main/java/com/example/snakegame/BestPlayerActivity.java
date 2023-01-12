package com.example.snakegame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BestPlayerActivity extends AppCompatActivity {

    TextView dateTimeTV;

    ImageView trophyIV;

    DataBaseHelper dataBaseHelper;

    ArrayAdapter playerArrayAdapter;

    ListView bestListView;

    Button newGame1Btn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_player);

        getSupportActionBar().setTitle("Snake Game Best Player");

        bestListView = findViewById(R.id.bestListView);
        dateTimeTV = findViewById(R.id.dateTimeTV);
        trophyIV = findViewById(R.id.trophyIV);

        dataBaseHelper = new DataBaseHelper(BestPlayerActivity.this);
        playerArrayAdapter = new ArrayAdapter<PlayerModel>(BestPlayerActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getBest());
        bestListView.setAdapter(playerArrayAdapter);

        //Toast.makeText(getApplicationContext(), "Don't press on image",Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "Press image to see GitHub project",Toast.LENGTH_LONG).show();

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);

        dateTimeTV.setText(formattedDateTime);

        trophyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gitHub = new Intent();
                gitHub.setAction(Intent.ACTION_VIEW);
                gitHub.addCategory(Intent.CATEGORY_BROWSABLE);
                gitHub.setData(Uri.parse("https://github.com/Crocodilu/SnakeGame"));
                startActivity(gitHub);

                /*
                Intent killApp = new Intent(BestPlayerActivity.this, MainActivity.class);
                killApp.putExtra("kill", true);
                startActivity(killApp);
                 */
            }
        });

        newGame1Btn = findViewById(R.id.newGame1Btn);
        newGame1Btn.setBackgroundColor(Color.CYAN);
        newGame1Btn.setTextColor(Color.BLACK);

        newGame1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gameOver = new Intent(BestPlayerActivity.this, MainActivity.class);
                startActivity(gameOver);
            }
        });
    }// onCreate
}