package com.appsnipp.EasyConference;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private BackKeyClickHandler backKeyClickHandler;
    private final boolean validate = false;
    private DateBaseHelper dbHelper;
    private SQLiteDatabase sqliteDatabase;
    private Cursor cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리스트뷰 선언
        ListView conferenceListView = (ListView) findViewById(R.id.conferenceListView);

        //SQLite 메소드 호출
        DateBaseHelper dbHelper = new DateBaseHelper(MainActivity.this, "Conference.db", null, 1);
        sqliteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqliteDatabase);

        //뒤로가기 메소드 호출
        backKeyClickHandler = new BackKeyClickHandler(MainActivity.this);

        //현재 날짜 표출
        Date Date = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(Date);

        TextView TopCurrentDate = (TextView) findViewById(R.id.TopCurrentDate);
        TopCurrentDate.setText(currentDate);
    }

    //뒤로가기
    public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }
}
