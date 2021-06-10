package com.appsnipp.EasyConference;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity {

    private DateBaseHelper dbHelper;
    private SQLiteDatabase sqliteDatabase;
    private static final String TAG = "WriteActivity";
    private BackKeyClickHandler backKeyClickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //SQLite 메소드 호출
        DateBaseHelper dbHelper = new DateBaseHelper(WriteActivity.this, "Conference.db", null, 1);
        sqliteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqliteDatabase);

        //현재 날짜 표출
        Date Date = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date);

        EditText CurrentDate = (EditText) findViewById(R.id.regdate);
        CurrentDate.setText(currentDate);
        CurrentDate.setClickable(false);
        CurrentDate.setFocusable(false);

        //뒤로가기 메소드 호출
        backKeyClickHandler = new BackKeyClickHandler(WriteActivity.this);
    }

    public void backPage(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //뒤로가기
    public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }
}
