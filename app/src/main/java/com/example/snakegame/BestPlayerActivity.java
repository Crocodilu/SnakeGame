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

        // used to bind to CurrentTimeService and retrieve its instance to access its public methods
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

        getSupportActionBar().setTitle("Snake Game - Best Player");

        bestListView = findViewById(R.id.bestListView);
        timeTV = findViewById(R.id.timeTV);
        dateTV = findViewById(R.id.dateTV);

        // update and display list content - in this case, only one element - best player
        dataBaseHelper = new DataBaseHelper(BestPlayerActivity.this);
        playerArrayAdapter = new ArrayAdapter<PlayerModel>(BestPlayerActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getBest());
        bestListView.setAdapter(playerArrayAdapter);


        gitBtn = findViewById(R.id.gitBtn);
        trophyBtn = findViewById(R.id.trophyBtn);
        exitBtn = findViewById(R.id.exitBtn);

        // used to manage the fragments in an activity
        // used to add, remove, replace, or show/hide fragments in the activity
        // manages the back stack of fragments, so that the user can navigate back to the previous fragment by pressing the back button
        FragmentManager fragmentManager = getSupportFragmentManager();

        gitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // perform a transaction that replaces the current fragment with a new instance (of GitFragment in this case)
                fragmentManager
                        // starts a new fragment transaction
                        .beginTransaction()
                        // replaces the current fragment in the container view (with an instance of GitFragment class)
                        .replace(R.id.fragmentContainerView, GitFragment.class, null)
                        // transaction can be optimized to reduce the number of changes needed to perform the transaction
                        .setReorderingAllowed(true)
                        // adds the transaction to the back stack, so that the user can navigate back to the previous fragment
                        // by pressing the back button
                        .addToBackStack("") // name can be null
                        // commits the transaction to the FragmentManager, which applies the changes to the UI
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


        // we use a service to display the exact time of the device
        Intent intent = new Intent(this, CurrentTimeService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        timeTV.setText("System time: " + currentTimeService.getSystemTime());

        // connect to the nist server to display the exact date
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


    // class retrieves the current date and time from the National Institute of Standards and
    // Technology (NIST) time server using the Network Time Protocol (NTP)
    class NistTimeClient implements Runnable{

        public NistTimeClient() {}

        @Override
        public void run() {
            try {
                // connect to the NIST time server using its IP address and port number
                Socket socket = new Socket("time.nist.gov", 13);
                // read the response from the NIST server
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // read the first line from the server response to discard it
                bufferedReader.readLine();
                // read the second line, which contains the current date and time in the format YY-MM-DD hh:mm:ss
                // we only take the date from this line with substring() method
                String currentTime = bufferedReader.readLine().substring(6, 14);
                socket.close();

                // update the UI
                runOnUiThread(new Runnable() {
                    @Override
                    // setting the text of dateTextView
                    public void run() {
                        dateTV.setText("NIST server date: 20" + currentTime);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//NistTimeClient
}// BestPlayerActivity class