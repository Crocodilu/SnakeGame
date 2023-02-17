package com.example.snakegame;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// provides a simple functionality of getting the current system time
// and making it available to the client that binds to it
public class CurrentTimeService extends Service {
    public CurrentTimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public MyBinder myBinder = new MyBinder();
    public class MyBinder extends Binder{
        CurrentTimeService getCurrentTimeService(){
            return CurrentTimeService.this;
        }
    }

    // returns the current system time in a String format using the SimpleDateFormat class
    public String getSystemTime(){
        java.text.SimpleDateFormat systemTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        return systemTime.format(new Date());
    }
}