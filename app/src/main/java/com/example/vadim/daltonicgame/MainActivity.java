package com.example.vadim.daltonicgame;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer mTimer;
    private TextView mTimerTextView;
    private TextView mQuestionTextView;
    private Random random;
    private int timeAfterStart = 60;
    final int DELAY = 1000;
    final int PERIOD = 1000;
    private int[] COLORS;
    private String[] COLORS_TEXT;
    private Button[] mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        COLORS = new int[]{getColor(R.color.blue), getColor(R.color.green),
                getColor(R.color.pink), getColor(R.color.purple),
                getColor(R.color.yellow), getColor(R.color.red),
                getColor(R.color.orange), getColor(R.color.brown) };
        COLORS_TEXT = new String[]{getString(R.string.blue), getString(R.string.green),
                getString(R.string.pink), getString(R.string.purple),
                getString(R.string.yellow), getString(R.string.red),
                getString(R.string.orange), getString(R.string.brown)};
        mQuestionTextView = findViewById(R.id.questionTextView);
        mTimerTextView = findViewById(R.id.timerTextView);

        mButtons = new Button[8];
        mButtons[0] = findViewById(R.id.answerButton1);
        mButtons[1] = findViewById(R.id.answerButton2);
        mButtons[2] = findViewById(R.id.answerButton3);
        mButtons[3] = findViewById(R.id.answerButton4);
        mButtons[4] = findViewById(R.id.answerButton5);
        mButtons[5] = findViewById(R.id.answerButton6);
        mButtons[6] = findViewById(R.id.answerButton7);
        mButtons[7] = findViewById(R.id.answerButton8);


        random = new Random();

        mTimer = new Timer();

        mTimerTextView.setText(String.valueOf(timeAfterStart));
        initButtons();
        startGame();
    }

    private void initButtons(){
        int[] usedColors = new int[8];
        int used = 0;
        for (int i = 0; i < mButtons.length; i++){
            int color = COLORS[random.nextInt(COLORS.length)];
            boolean isNotRepeated = false;

            while (!isNotRepeated) {
                isNotRepeated = true;
                for (int j = 0; j < used; j++) {
                      if (color == usedColors[j]) {
                          color = COLORS[random.nextInt(COLORS.length)];
                          isNotRepeated = false;
                      }
                }
            }
            usedColors[used] = color;
            used++;
            mButtons[i].setBackgroundColor(color);
            mButtons[i].setOnClickListener(new buttonsClickListener());
        }
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
        changeQuestion();
    }

    private void changeQuestion(){
        int backgroundColor = COLORS[random.nextInt(COLORS.length)];
        int textColor = COLORS[random.nextInt(COLORS.length)];
        while (textColor == backgroundColor)
            textColor = COLORS[random.nextInt(COLORS.length)];
        mQuestionTextView.setBackgroundColor(backgroundColor);
        mQuestionTextView.setTextColor(textColor);
        mQuestionTextView.setText(COLORS_TEXT[random.nextInt(mButtons.length)]);
    }

    private void checkAnswer(){

    }

    private class buttonsClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            ColorDrawable backgroundColor = (ColorDrawable)view.getBackground();
            Log.d("btncolor", String.valueOf( mQuestionTextView.getText()));
            Log.d("redcolor", String.valueOf(getColor(R.color.orange)));
            if (backgroundColor.getColor() == getColor(R.color.red) && mQuestionTextView.getText().equals(getString(R.string.red)) ||
                    backgroundColor.getColor() == getColor(R.color.orange) && mQuestionTextView.getText().equals(getString(R.string.orange)) ||
                    backgroundColor.getColor() == getColor(R.color.blue) && mQuestionTextView.getText().equals(getString(R.string.blue)) ||
                    backgroundColor.getColor() == getColor(R.color.brown) && mQuestionTextView.getText().equals(getString(R.string.brown)) ||
                    backgroundColor.getColor() == getColor(R.color.green) && mQuestionTextView.getText().equals(getString(R.string.green)) ||
                    backgroundColor.getColor() == getColor(R.color.pink) && mQuestionTextView.getText().equals(getString(R.string.pink)) ||
                    backgroundColor.getColor() == getColor(R.color.yellow) && mQuestionTextView.getText().equals(getString(R.string.yellow)) ||
                    backgroundColor.getColor() == getColor(R.color.purple) && mQuestionTextView.getText().equals(getString(R.string.purple))){


                



            }
        }
    }
}