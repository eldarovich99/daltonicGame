package com.example.vadim.daltonicgame;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer mTimer;
    private TextView mTimerTextView;
    private TextView mQuestionTextView;
    private TextView mRightAnswers;
    private Random random;
    private final int GAME_DURATION = 60;
    private int remainingTime;
    final int DELAY = 1000;
    final int PERIOD = 1000;
    private int[] COLORS;
    private int rightAnswers;
    private String[] COLORS_TEXT;
    private Button[] mButtons;
    private Button mStartButton;
    private Button mStopButton;
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
        mRightAnswers = findViewById(R.id.rightAnswers);


        mButtons = new Button[8];
        mButtons[0] = findViewById(R.id.answerButton1);
        mButtons[1] = findViewById(R.id.answerButton2);
        mButtons[2] = findViewById(R.id.answerButton3);
        mButtons[3] = findViewById(R.id.answerButton4);
        mButtons[4] = findViewById(R.id.answerButton5);
        mButtons[5] = findViewById(R.id.answerButton6);
        mButtons[6] = findViewById(R.id.answerButton7);
        mButtons[7] = findViewById(R.id.answerButton8);
        mStartButton = findViewById(R.id.startButton);
        mStopButton = findViewById(R.id.stopButton);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimer.cancel();
                remainingTime = 0;
                mTimerTextView.setText(String.valueOf(remainingTime));
                endGame();
            }
        });

        random = new Random();

        mTimerTextView.setText(String.valueOf(remainingTime));
        mRightAnswers.setText(String.valueOf(rightAnswers));
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

    private void endGame(){
        String message = getString(R.string.right_answers) + rightAnswers;
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        updateDatabase(rightAnswers);
    }

    private void updateDatabase(int score){
        SQLiteDatabase database = getBaseContext().openOrCreateDatabase("rating.db", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS users (date TEXT, score INTEGER)");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a", Locale.getDefault());
        String date = simpleDateFormat.format(calendar.getTime());
        String addingRecord = "INSERT INTO users VALUES ('" + date + "', " + String.valueOf(score) + ")";
        database.execSQL(addingRecord);
        database.close();
    }

    private void startGame(){
        rightAnswers = 0;
        mRightAnswers.setText("0");
        remainingTime = GAME_DURATION;

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                remainingTime -=1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimerTextView.setText(String.valueOf(remainingTime));
                        if (remainingTime == 0) {
                            mTimer.cancel();
                            endGame();
                        }
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

    private boolean checkAnswer(ColorDrawable backgroundColor){
        return(backgroundColor.getColor() == getColor(R.color.red) && mQuestionTextView.getText().equals(getString(R.string.red)) ||
                backgroundColor.getColor() == getColor(R.color.orange) && mQuestionTextView.getText().equals(getString(R.string.orange)) ||
                backgroundColor.getColor() == getColor(R.color.blue) && mQuestionTextView.getText().equals(getString(R.string.blue)) ||
                backgroundColor.getColor() == getColor(R.color.brown) && mQuestionTextView.getText().equals(getString(R.string.brown)) ||
                backgroundColor.getColor() == getColor(R.color.green) && mQuestionTextView.getText().equals(getString(R.string.green)) ||
                backgroundColor.getColor() == getColor(R.color.pink) && mQuestionTextView.getText().equals(getString(R.string.pink)) ||
                backgroundColor.getColor() == getColor(R.color.yellow) && mQuestionTextView.getText().equals(getString(R.string.yellow)) ||
                backgroundColor.getColor() == getColor(R.color.purple) && mQuestionTextView.getText().equals(getString(R.string.purple)));
    }

    private class buttonsClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            ColorDrawable backgroundColor = (ColorDrawable)view.getBackground();
            if (remainingTime > 0){
                if (checkAnswer(backgroundColor)){
                        rightAnswers++;
                        mRightAnswers.setText(String.valueOf(rightAnswers));
                }
                changeQuestion();
            }
            else{
                Toast.makeText(MainActivity.this, getString(R.string.time_is_over), Toast.LENGTH_SHORT).show();
            }
        }
    }
}