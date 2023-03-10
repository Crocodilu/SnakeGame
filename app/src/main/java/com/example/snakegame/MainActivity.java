package com.example.snakegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    // list of snake points / snake length
    private final List<SnakePoints> snakePointsList = new ArrayList<>();

    // type of View to display images, videos, or other content
    // is implemented as a separate window which sits on top of the main layout window
    // is used to draw graphics and animations
    private SurfaceView surfaceView;

    private TextView scoreTV;

    // surface holder to draw snake on surface's canvas
    private SurfaceHolder surfaceHolder;

    // snake moving position; values must be right, left, top, bottom
    // by default snake moves to right
    private String movingPosition = "right";

    // score
    private int score = 0;

    // snake size / point size
    private static final int pointSize = 28;

    // default snake tale
    private static final int defaultTalePoints = 3;

    // snake color
    private static final int snakeColor = Color.YELLOW;

    // snake moving speed; values between 1 - 1000
    private static final int snakeMovingSpeed = 800;

    // random point position coordinates on the surfaceView
    private int positionX = 0, positionY = 0;

    // timer to move snake / change snake position after a specific time (snakeMovingSpeed)
    private Timer timer;

    // canvas to draw snake and show on surface view
    private Canvas canvas = null;

    // point color / single point color of a snake
    private Paint pointColor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide action bar
        getSupportActionBar().hide();

        // to exit the application from BestPlayerActivity - ExitFragment
        if(getIntent().getBooleanExtra("kill", false)){
            // remove all the activities that share the same affinity
            // in this case i haven???t defined one so it???s the same for all
            finishAffinity();
        }

        // getting surfaceView and scoreTextView from xml file
        surfaceView = findViewById(R.id.surfaceView);
        scoreTV = findViewById(R.id.scoreTV);

        // getting ImageButtons from xml file
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);

        // adding callback to surfaceView
        surfaceView.getHolder().addCallback(this);

        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if previous moving position is not bottom; snake can't move;
                // for example if snake moving bottom then snake can't directly start moving to top
                // snake must take right or left first, then top
                if(!movingPosition.equals("bottom")){
                    movingPosition = "top";
                }
            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!movingPosition.equals("right")){
                    movingPosition = "left";
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!movingPosition.equals("left")){
                    movingPosition = "right";
                }
            }
        });

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!movingPosition.equals("top")){
                    movingPosition = "bottom";
                }
            }
        });
    }// onCreate

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        // when surface is created
        // then get surfaceHolder from it and assign to surfaceHolder
        this.surfaceHolder = surfaceHolder;

        // init data for snake / surfaceView
        init();
    }// surfaceCreated

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void init(){


        // clear snake points / snake length
        snakePointsList.clear();

        // ste default score as 0
        scoreTV.setText("0");

        // make score 0
        score = 0;

        // setting default moving position
        movingPosition = "right";

        // default snake starting position on screen
        int startPositionX =(pointSize) * defaultTalePoints;

        // making snake's default length / points
        for(int i = 0; i < defaultTalePoints; i++){

            // adding points to snake's tale
            SnakePoints snakePoints = new SnakePoints(startPositionX, pointSize);
            snakePointsList.add(snakePoints);

            // increasing value for next point as snake's tale
            startPositionX = startPositionX - (pointSize * 2);
        }// for

        // add random point om the screen to be eaten by the snake
        addPoint();

        // start moving snake / start game
        moveSnake();
    }// init

    private void addPoint(){

        // getting surfaceView width and height to add point on the surface to be eaten by the snake
        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        // generate two random positions for the next point
        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        // check if randomPosition is even or odd value; we need only even number
        if((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;
        }

        if((randomYPosition % 2) != 0){
            randomYPosition = randomYPosition + 1;
        }

        positionX = (pointSize * randomXPosition) + pointSize;
        positionY = (pointSize * randomYPosition) + pointSize;
    }// addPoint

    private void moveSnake(){

        // Timer class is a utility class in Java that can be used to schedule tasks to run at a
        // specific time or to run repeatedly at a set interval
        timer = new Timer();

        // scheduleAtFixedRate method is called on the timer object,
        // which is used to schedule task to run at a fixed rate
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // getting head position
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();

                // check if snake eaten point
                if (headPositionX == positionX && headPositionY == positionY){

                    // grow snake after eaten point
                    growSnake();

                    // add another random point on the screen
                    addPoint();
                }// if

                // check of witch side snake is moving
                switch (movingPosition){
                    case "right":

                        // move snake's head to right
                        // other points follow snake's head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);

                        break;

                    case "left":

                        // move snake's head to left
                        // other points follow snake's head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);

                        break;

                    case "top":

                        // move snake's head to top
                        // other points follow snake's head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY - (pointSize * 2));

                        break;

                    case "bottom":

                        // move snake's head to bottom
                        // other points follow snake's head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));

                        break;
                }// switch

                // check if game is over; game over when snake touches edges or his tail
                if(checkGameOver(headPositionX, headPositionY)){

                    // stop timer / stop moving snake
                    timer.purge();
                    timer.cancel();

                    // send score and username to GameOverActivity
                    // data is saved in database only if player goes to GameOverActivity (click on See info)
                    Intent gameOver = new Intent(MainActivity.this, GameOverActivity.class);
                    gameOver.putExtra("score", score);
                    gameOver.putExtra("username", getIntent().getStringExtra("username"));

                    // show game over dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Your score is " + score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);

                    builder.setPositiveButton("          See info", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(gameOver);
                        }
                    });

                    builder.setNegativeButton("Start again          ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            builder.setView(surfaceView);
                            // restart game / re-init data
                            init();
                        }
                    });

                    // timer runs in background so we need to show dialog on main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }// if

                else{

                    // lock canvas on surfaceHolder to draw it
                    canvas = surfaceHolder.lockCanvas();

                    // clear canvas with white color
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    // change snake's head position; other snake points will follow snake's head
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createPointColor());

                    // draw random point circle on the surface to be eaten by the snake
                    canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                    // other points are following snake's head; position 0 is snake's head
                    for(int i = 1; i < snakePointsList.size(); i++){

                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();

                        // move points across head
                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(), pointSize, createPointColor());

                        // change head position
                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }

                    // unlock canvas to draw on surface view
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000 - snakeMovingSpeed, 1000 - snakeMovingSpeed);
    }// moveSnake

    private void growSnake(){

        // create new snake point
        SnakePoints snakePoints = new SnakePoints(0, 0);

        // add point to the snake's tale
        snakePointsList.add(snakePoints);

        // increase score
        score++;

        // setting score to TextView
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTV.setText(String.valueOf(score));
            }
        });
    }// growSnake

    private boolean checkGameOver(int headPositionX, int headPositionY){
        boolean gameOver = false;

        // check if snake's head touches edges
        if(snakePointsList.get(0).getPositionX() < 0 ||
                snakePointsList.get(0).getPositionY() < 0 ||
                snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() ||
                snakePointsList.get(0).getPositionY() >= surfaceView.getHeight())
        {
            gameOver = true;
        }
        else{

            // check if snake's head touches his tail
            for(int i = 1; i < snakePointsList.size(); i++){

                if(headPositionX == snakePointsList.get(i).getPositionX() &&
                headPositionY == snakePointsList.get(i).getPositionY())
                {
                    gameOver = true;
                    break;
                }
            }
        }
        return gameOver;
    }// checkGameOver

    private Paint createPointColor(){

        // check if color not defined before
        if(pointColor == null){
            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true); // smoothness
        }

        return pointColor;
    }// createPointColor
}// MainActivity class