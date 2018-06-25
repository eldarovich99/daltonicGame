package com.example.vadim.daltonicgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer mTimer;
    private TextView mTimerTextView;

    private int timeAfterStart = 60;
    final int DELAY = 1000;
    final int PERIOD = 1000;

    private final int[] COLORS = {getColor(R.color.blue), getColor(R.color.green),
            getColor(R.color.pink), getColor(R.color.purple),
            getColor(R.color.yellow), getColor(R.color.red) };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mTimerTextView = findViewById(R.id.timerTextView);
        mTimer = new Timer();

        mTimerTextView.setText(String.valueOf(timeAfterStart));
        startGame();
    }
    private void startGame(){
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeAfterStart -=1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimerTextView.setText(String.valueOf(timeAfterStart));
                        if (timeAfterStart == 0)
                            mTimer.cancel();
                    }
                });
            }
        }, 0, PERIOD);
    }

    private void changeQuestion(){
    }

}