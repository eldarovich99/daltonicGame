package com.example.vadim.daltonicgame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    SQLiteDatabase database;
    RecyclerView mRecyclerView;
    List<Record> mRecords = new ArrayList<>();
    private Button mClearButton;
    DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        init();
    }

    private void init(){
        mRecyclerView = findViewById(R.id.topRecyclerView);
        setInitialData();
        mAdapter = new DataAdapter(this, mRecords);
        mRecyclerView.setAdapter(mAdapter);
        mClearButton = findViewById(R.id.ratingClearButton);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = getBaseContext().openOrCreateDatabase("rating.db", MODE_PRIVATE, null);
                database.execSQL("DROP TABLE IF EXISTS top");
                database.close();
                mRecords.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setInitialData() {

        database = getBaseContext().openOrCreateDatabase("rating.db", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS top (date TEXT, name TEXT, score INTEGER)");
        Cursor query = database.rawQuery("SELECT * FROM top", null);
        if (query.moveToLast()){
           do{
               Record record = new Record(query.getString(1), query.getString(0), query.getInt(2));
               mRecords.add(record);
           }  while (query.moveToPrevious());
        }
        database.close();


        //Record record = new Record("Vados", "04.07.2018", 5);
        //mRecords.add(record);
    }
}
