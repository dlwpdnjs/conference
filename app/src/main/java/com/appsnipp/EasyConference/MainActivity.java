package com.appsnipp.EasyConference;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

        //SQLite 메소드 호출
        DateBaseHelper dbHelper = new DateBaseHelper(MainActivity.this, "Conference.db", null, 1);
        sqliteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqliteDatabase);

        //뒤로가기 메소드 호출
        backKeyClickHandler = new BackKeyClickHandler(MainActivity.this);

    }
}
