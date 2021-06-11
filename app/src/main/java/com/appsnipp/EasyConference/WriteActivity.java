package com.appsnipp.EasyConference;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity {

    private DateBaseHelper dbHelper;
    private SQLiteDatabase sqliteDatabase;
    private static final String TAG = "WriteActivity";
    private BackKeyClickHandler backKeyClickHandler;
    private Cursor cu;

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

    //회의록 저장
    public void saveConference(View v) {
        AlertDialog dialog;

        EditText edititle = findViewById(R.id.title);
        EditText ediregdate = findViewById(R.id.regdate);
        EditText edipeoples = findViewById(R.id.peoples);
        EditText edilocation = findViewById(R.id.location);
        EditText edicontent = findViewById(R.id.content);

        try {
            String title = edititle.getText().toString();
            String regdate = ediregdate.getText().toString();
            String peoples = edipeoples.getText().toString();
            String location = edilocation.getText().toString();
            String content = edicontent.getText().toString();

            //유효성 검사
            if(title.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                dialog = builder.setMessage("주제를 입력해주세요.").setNegativeButton("확인", null).create();
                dialog.show();

                return;
            }

            String getUserInfo = "select name from tb_users";
            cu = sqliteDatabase.rawQuery(getUserInfo,null);

            cu.moveToNext();
            String reguser = cu.getString(0);

            ContentValues values = new ContentValues();
            values.put("Cnf_title", title);
            values.put("Cnf_regdate", regdate);
            values.put("Cnf_attendants", peoples);
            values.put("Cnf_location", location);
            values.put("Cnf_content", content);
            values.put("Cnf_reguser", reguser);
            long result = sqliteDatabase.insert("TB_CONFERENCE",null, values);

            if(result > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                dialog = builder.setMessage("저장이 완료되었습니다.").setNegativeButton("확인", null).create();
                dialog.show();

                //메인화면으로 이동
                backPage(v);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "저장중 에러가 발생하였습니다.");
            Log.e(TAG, "error", e);
        }
    }
}
