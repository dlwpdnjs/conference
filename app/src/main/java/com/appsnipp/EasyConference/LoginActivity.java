package com.appsnipp.EasyConference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
        if (UserEmail.equals("") || UserPwd.equals("") || UserName.equals("") || UserId.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder.setMessage("모든 값을 입력해주세요.").setNegativeButton("확인", null).create();
            dialog.show();

            return;
        }
        else {
            try {
                //Db에 Users 데이터 Insert
                ContentValues values = new ContentValues();
                values.put("name", UserName);
                values.put("email", UserEmail);
                values.put("id", UserPwd);
                values.put("password", UserPwd);
                long result = sqliteDatabase.insert("TB_USERS",null,values);

                //성공 시 TB_USERS 조회
                if(result > 0) {
                    String getUserInfo = "select * from TB_USERS";
                    Cursor cu = sqliteDatabase.rawQuery(getUserInfo,null);
                    while(cu.moveToNext()) {
                        System.out.println("userId :" +cu.getString(cu.getColumnIndex("id")));
                    }

                    //회원가입 성공 팝업
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("회원가입이 완료되었습니다.").setNegativeButton("확인", null).create();
                    dialog.show();

                    //로그인화면으로 이동
                    findViewById(R.id.registView).setVisibility(View.GONE);
                    findViewById(R.id.loginView).setVisibility(View.VISIBLE);
                }

            }catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "데이터 입력중 에러가 발생하였습니다");
            }
        }
    }
}