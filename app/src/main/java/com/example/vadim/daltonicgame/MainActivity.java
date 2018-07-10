package com.example.vadim.daltonicgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView mRightAnswersTextView;
    private Random random;
    private final int GAME_DURATION = 25;
    private int remainingTime;
    final int DELAY = 1000;
    final int PERIOD = 1000;
    private int[] COLORS;
    private int mRightAnswers;
    private int mClicks;
    private int mGameMode;
    private double mPercentage;
    private String[] COLORS_TEXT;
    private Button[] mButtons;
    private Button mStartButton;
    private Button mStopButton;
    private EditText mNameEditText;
    private final Context mContext = this;
    private String nameOfRecordsman;
    private String mPreviousQuestion;
    private SharedPreferences mSharedPreferences;
    private final String APP_PREFERENCES = "settings";
    private final String GAME_MODE = "game_mode";
    private final int[] mGameModes = {0,1,2};


    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mSharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSharedPreferences.contains(GAME_MODE))
            mGameMode = mSharedPreferences.getInt(GAME_MODE, Context.MODE_PRIVATE);
        else mGameMode = mGameModes[1];
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
        mRightAnswersTextView = findViewById(R.id.rightAnswers);


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
                remainingTime = 0;
                mTimerTextView.setText(String.valueOf(remainingTime));
                endGame();
            }
        });

        random = new Random();

        mTimerTextView.setText(String.valueOf(remainingTime));
        mRightAnswersTextView.setText(String.valueOf(mRightAnswers));
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
        mTimer.cancel();
        String message = getString(R.string.right_answers) + mRightAnswers;
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        database = getBaseContext().openOrCreateDatabase("rating.db", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS top (date TEXT, name TEXT, percentage TEXT, score INTEGER)");
        double clicks = (double) mClicks;
        mPercentage = mRightAnswers / clicks * 100;
        if (isBiggerScore(database, mRightAnswers)) {
            updateDatabase();
        }
        database.close();
    }

    private void updateDatabase(){
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View addRecordView = layoutInflater.inflate(R.layout.add_record, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext);
            mDialogBuilder.setView(addRecordView);
            mNameEditText = addRecordView.findViewById(R.id.input_text);
            mDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database = getBaseContext().openOrCreateDatabase("rating.db", MODE_PRIVATE, null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS top (date TEXT, name TEXT, percentage TEXT,score INTEGER)");
                        nameOfRecordsman = "'" + mNameEditText.getText().toString() + "'";
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm ", Locale.getDefault());
                        String date = simpleDateFormat.format(calendar.getTime());
                    Log.d("clicks", String.valueOf(mClicks));
                        String percentageString = String.format("%.2f", mPercentage);

                        String addingRecord = "INSERT INTO top VALUES ('" + date + "', " + nameOfRecordsman + ", '" + percentageString + "', " + String.valueOf(mRightAnswers) + ")";
                        database.execSQL(addingRecord);
                        // debug info
                        Cursor query = database.rawQuery("SELECT * FROM top", null);
                        if (query.moveToFirst())
                            do {
                                Log.d("db", query.getString(0) + query.getString(1) + String.valueOf(query.getString(2)) + String.valueOf(query.getInt(3)));
                            }
                            while (query.moveToNext());
                    database.close();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.show();


        //database.execSQL("DROP TABLE IF EXISTS top");
    }

    private boolean isBiggerScore(SQLiteDatabase database, int score){
        Cursor query = database.rawQuery("SELECT * FROM top",null);
        int max = 0;
        if (query.moveToFirst())
            do {
                int currentScore = query.getInt(3);
                if (currentScore > max) max = currentScore;
            }
            while (query.moveToNext());
        return score > max;
    }

    private void startGame(){
        mRightAnswers = 0;
        mClicks = 0;
        mRightAnswersTextView.setText("0");
        if (remainingTime > 0) mTimer.cancel();
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
                        if (remainingTime <= 0) {
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
        if (mGameMode != mGameModes[0]) mQuestionTextView.setBackgroundColor(backgroundColor);
        if (mGameMode == mGameModes[2]) initButtons();
        mQuestionTextView.setTextColor(textColor);
        String currentQuestion = COLORS_TEXT[random.nextInt(mButtons.length)];
        if (mPreviousQuestion != null && currentQuestion == mPreviousQuestion) {
            while (currentQuestion == mPreviousQuestion)
                currentQuestion = COLORS_TEXT[random.nextInt(mButtons.length)];
        }
        mQuestionTextView.setText(currentQuestion);
        mPreviousQuestion = currentQuestion;

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
            mClicks++;
            ColorDrawable backgroundColor = (ColorDrawable)view.getBackground();
            if (remainingTime > 0){
                if (checkAnswer(backgroundColor)){
                        mRightAnswers++;
                        mRightAnswersTextView.setText(String.valueOf(mRightAnswers));
                }
                changeQuestion();
            }
            else{
                Toast.makeText(MainActivity.this, getString(R.string.time_is_over), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }
}