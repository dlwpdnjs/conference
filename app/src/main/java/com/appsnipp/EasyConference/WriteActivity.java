package com.appsnipp.EasyConference;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
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

    //달력 호출
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    String date = "";
                    String year = "";
                    String month = "";
                    String day = "";
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    TextView tv = findViewById(R.id.regdate);
                    if(mm > 0 && mm < 10) {
                        month = "0"+ String.valueOf(mm);
                        mm = Integer.parseInt(month);
                    }
                    if(dd > 0 && dd < 10) {
                        day = "0"+ String.valueOf(dd);
                        dd = Integer.parseInt(day);
                    }
                    year = String.valueOf(yy);
                    date = year+"-"+month+"-"+day;
                    tv.setText(date);
                }
            };

    public void mOnClick_DatePick(View v){
        // DATE Picker가 처음 떴을 때, 오늘 날짜가 보이도록 설정.
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }

    public void backPage(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //뒤로가기
    /*public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }*/
}
