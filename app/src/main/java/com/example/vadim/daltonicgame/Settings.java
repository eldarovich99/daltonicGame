package com.example.vadim.daltonicgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings extends AppCompatActivity {
    private RadioGroup mRadioGroup;
    private Button mOkButton;
    private Button mCancelButton;
    private RadioButton[] mRadioButtons;
    private final int[] mGameModes = {0,1,2};
    private int mCurrentGameMode;
    public final String APP_PREFERENCES = "settings";
    public final String GAME_MODE = "game_mode";
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }
    private void init(){
        mRadioGroup = findViewById(R.id.settings_radio_group);
        mOkButton = findViewById(R.id.settings_ok_button);
        mCancelButton = findViewById(R.id.setting_cancel_button);
        mRadioButtons = new RadioButton[]{findViewById(R.id.first_mode_radio_button),
                findViewById(R.id.second_mode_radio_button), findViewById(R.id.third_mode_radio_button)};

        mSharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSharedPreferences.contains(GAME_MODE))
            mCurrentGameMode = mSharedPreferences.getInt(GAME_MODE, Context.MODE_PRIVATE);
        else mCurrentGameMode = mGameModes[1];

        mRadioButtons[mCurrentGameMode].setChecked(true);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.first_mode_radio_button:
                        mCurrentGameMode = mGameModes[0];
                        break;
                    case R.id.second_mode_radio_button:
                        mCurrentGameMode = mGameModes[1];
                        break;
                    case R.id.third_mode_radio_button:
                        mCurrentGameMode = mGameModes[2];
                        break;
                }
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences.edit().putInt(GAME_MODE, mCurrentGameMode).apply();
            }
        });

    }
}
