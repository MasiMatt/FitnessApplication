package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Class that starts a timer
 */

import android.os.Handler;
import android.os.SystemClock;

public class Timer {
    private long startTime;
    private Handler customHandler;
    private long timeInMilliseconds;
    private long timeSwapBuff;
    private long updatedTime;
    private int mins;
    private int secs;
    private int milliseconds;
    private int totalSeconds;

    // constructor
    public Timer(){
        startTime = 0L;
        customHandler = new Handler();
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;
        mins = 0;
        secs = 0;
        milliseconds = 0;
        totalSeconds = 0;
    }

    // starts timer thread
    public void startTimer(){
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    // stops timer thread
    public void stopTimer(){
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    // returns time in seconds
    public int getTime(){
        return totalSeconds;
    }

    // thread that calculates the time
    public Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            totalSeconds = (int) (updatedTime / 1000);
            secs = totalSeconds;
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedTime % 1000);
            MapsActivity.timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };
}
