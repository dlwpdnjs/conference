package com.appsnipp.EasyConference;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    private DateBaseHelper dbHelper;
    private SQLiteDatabase sqliteDatabase;
    private static final String TAG = "UpdateActivity";
    private BackKeyClickHandler backKeyClickHandler;
    private Cursor cu;

    long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        EditText title = (EditText) findViewById(R.id.title);
        EditText regdate = (EditText) findViewById(R.id.regdate);
        EditText peoples = (EditText) findViewById(R.id.peoples);
        EditText location = (EditText) findViewById(R.id.location);
        EditText content = (EditText) findViewById(R.id.content);

        //SQLite 메소드 호출
        DateBaseHelper dbHelper = new DateBaseHelper(UpdateActivity.this, "Conference.db", null, 1);
        sqliteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqliteDatabase);

        //뒤로가기 메소드 호출
        backKeyClickHandler = new BackKeyClickHandler(UpdateActivity.this);

        //메인화면에서 넘긴 파라미터 값 받기
        Intent intent = getIntent();

        System.out.println(intent.getIntExtra("id", 0));

        id = intent.getIntExtra("id", 0);
        title.setText(intent.getStringExtra("title"));
        regdate.setText(intent.getStringExtra("regdate"));
        peoples.setText(intent.getStringExtra("attendants"));
        location.setText(intent.getStringExtra("location"));
        content.setText(intent.getStringExtra("content"));
    }

    //메인화면으로 이동
    public void backPage(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //수정
    public void UptConference(View v) {
        AlertDialog dialog;

        EditText title = (EditText) findViewById(R.id.title);
        EditText regdate = (EditText) findViewById(R.id.regdate);
        EditText peoples = (EditText) findViewById(R.id.peoples);
        EditText location = (EditText) findViewById(R.id.location);
        EditText content = (EditText) findViewById(R.id.content);

        try {
            String Stitle = title.getText().toString();
            String Sregdate = regdate.getText().toString();
            String Speoples = peoples.getText().toString();
            String Slocation = location.getText().toString();
            String Scontent = content.getText().toString();

            //유효성 검사
            if(title.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                dialog = builder.setMessage("주제를 입력해주세요.").setNegativeButton("확인", null).create();
                dialog.show();

                return;
            }

            ContentValues values = new ContentValues();
            values.put("Cnf_title", Stitle);
            values.put("Cnf_regdate", Sregdate);
            values.put("Cnf_attendants", Speoples);
            values.put("Cnf_location", Slocation);
            values.put("Cnf_content", Scontent);

            long result = sqliteDatabase.update("TB_CONFERENCE",values,"_id="+id,null);
            System.out.println("resultresultresult"+result);
            System.out.println("resultresultresult"+id);
            if(result > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                dialog = builder.setMessage("수정이 완료되었습니다.").setNegativeButton("확인", null).create();
                dialog.show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "저장중 에러가 발생하였습니다.");
            Log.e(TAG, "error", e);
        }
    }

    //삭제
    public void delConference(View v) {
        AlertDialog dialog;

        try {
            long result = sqliteDatabase.delete("TB_CONFERENCE","_id="+id, null);

            if(result > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                dialog = builder.setMessage("삭제하였습니다.").setNegativeButton("확인", null).create();
                dialog.show();

                //메인화면으로 이동
                backPage(v);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "삭제중 에러가 발생하였습니다.");
            Log.e(TAG, "error", e);
        }
    }

}
