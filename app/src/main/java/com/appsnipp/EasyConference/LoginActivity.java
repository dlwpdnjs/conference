package com.appsnipp.EasyConference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private BackKeyClickHandler backKeyClickHandler;

    private AlertDialog dialog;
    private boolean validate = false;
    private EditText join_email, join_password, join_name, join_id;
    private DateBaseHelper dbHelper;
    private SQLiteDatabase sqliteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //SQLite 메소드 호출
        dbHelper = new DateBaseHelper(LoginActivity.this, "Conference.db", null, 1);
        sqliteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqliteDatabase);

        //뒤로가기 메소드 호출
        backKeyClickHandler = new BackKeyClickHandler(this);

    }

    //뒤로가기
    @Override public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }

    //회원가입 화면으로 이동
    public void mvRegist(View v){
        findViewById(R.id.registView).setVisibility(View.VISIBLE);
        findViewById(R.id.loginView).setVisibility(View.GONE);
    }

    //로그인 이벤트
    public void login(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //회원가입 이벤트
    public void mvLogin(View v) throws Exception{
        //회원가입 값
        final EditText join_email = findViewById( R.id.joinEmail );
        final EditText join_password = findViewById( R.id.joinPw );
        final EditText join_name = findViewById( R.id.joinName );
        final EditText join_id = findViewById(R.id.joinId);

        String UserEmail = join_email.getText().toString();
        String UserPwd = join_password.getText().toString();
        String UserName = join_name.getText().toString();
        String UserId = join_id.getText().toString();

        //유효성 검사
        if (UserEmail.equals("") || UserPwd.equals("") || UserName.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
            dialog.show();

            return;
        }
        else {

            try {
                ContentValues values = new ContentValues();
                values.put("name","HelloAlpaca");
                values.put("email","HelloAlpaca");
                values.put("id","HelloAlpaca");
                values.put("password","HelloAlpaca");
                long result = sqliteDatabase.insert("TB_USERS",null,values);

                findViewById(R.id.registView).setVisibility(View.GONE);
                findViewById(R.id.loginView).setVisibility(View.VISIBLE);

            }catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "데이터 입력중 에러가 발생하였습니다");
            }
        }
    }
}