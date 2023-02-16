package com.example.snakegame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BestPlayerActivity extends AppCompatActivity {


    TextView timeTV;
    TextView dateTV;
    DataBaseHelper dataBaseHelper;
    ArrayAdapter playerArrayAdapter;
    ListView bestListView;
    Button newGame1Btn;
    Button gitBtn;
    Button trophyBtn;
    Button exitBtn;


    CurrentTimeService currentTimeService = new CurrentTimeService();
    boolean isConnected = false;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            CurrentTimeService.MyBinder myBinder = (CurrentTimeService.MyBinder) iBinder;
            currentTimeService = myBinder.getCurrentTimeService();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected = false;
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_player);

        getSupportActionBar().setTitle("Snake Game Best Player");

        bestListView = findViewById(R.id.bestListView);
        timeTV = findViewById(R.id.timeTV);
        dateTV = findViewById(R.id.dateTV);


        dataBaseHelper = new DataBaseHelper(BestPlayerActivity.this);
        playerArrayAdapter = new ArrayAdapter<PlayerModel>(BestPlayerActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getBest());
        bestListView.setAdapter(playerArrayAdapter);


        gitBtn = findViewById(R.id.gitBtn);
        trophyBtn = findViewById(R.id.trophyBtn);
        exitBtn = findViewById(R.id.exitBtn);

        FragmentManager fragmentManager = getSupportFragmentManager();

        gitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, GitFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("") // name can be null
                        .commit();
            }
        });

        trophyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, TrophyFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("") // name can be null
                        .commit();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ExitFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("") // name can be null
                        .commit();
            }
        });


        Toast.makeText(getApplicationContext(), "Press image for action",Toast.LENGTH_LONG).show();


        //Service pentru ora
        Intent intent = new Intent(this, CurrentTimeService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        timeTV.setText("System time: " + currentTimeService.getSystemTime());

        //Conectare la server pentru data
        BestPlayerActivity.NistTimeClient runnable = new BestPlayerActivity.NistTimeClient();
        new Thread(runnable).start();


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


    class NistTimeClient implements Runnable{

        public NistTimeClient() {}

        @Override
        public void run() {
            try {
                Socket socket = new Socket("time.nist.gov", 13);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bufferedReader.readLine();
                String currentTime = bufferedReader.readLine().substring(6, 14);
                socket.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dateTV.setText("NIST server date: 20" + currentTime);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//NistTimeClient
}