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
    private final boolean validate = false;
    private DateBaseHelper dbHelper;
    private SQLiteDatabase sqliteDatabase;
    private Cursor cu;

    String CId;
    String CPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //SQLite 메소드 호출
        DateBaseHelper dbHelper = new DateBaseHelper(LoginActivity.this, "Conference.db", null, 1);
        sqliteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqliteDatabase);

        //뒤로가기 메소드 호출
        backKeyClickHandler = new BackKeyClickHandler(LoginActivity.this);

    }

    //뒤로가기
    public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }

    //로그인 화면으로 이동
    public void backLogin(View v) {
        findViewById(R.id.registView).setVisibility(View.GONE);
        findViewById(R.id.loginView).setVisibility(View.VISIBLE);
        findViewById(R.id.backLogin).setVisibility(View.GONE);

        EditText join_email = findViewById(R.id.joinEmail);
        EditText join_password = findViewById(R.id.joinPw);
        EditText join_name = findViewById(R.id.joinName);
        EditText join_id = findViewById(R.id.joinId);

        join_email.setText(null);
        join_password.setText(null);
        join_name.setText(null);
        join_id.setText(null);
    }

    //회원가입 화면으로 이동
    public void mvRegist(View v){
        findViewById(R.id.registView).setVisibility(View.VISIBLE);
        findViewById(R.id.loginView).setVisibility(View.GONE);
        findViewById(R.id.backLogin).setVisibility(View.VISIBLE);
    }

    //로그인 이벤트
    public void login(View v) {
        //로그인 EditText
        EditText sign_id = findViewById(R.id.editTextId);
        EditText sign_password = findViewById(R.id.editTextPassword);

        try {
            String Id = sign_id.getText().toString();
            String Password = sign_password.getText().toString();

            String getUserInfo = "select id, password from tb_users where id='" +Id+"'";
            cu = sqliteDatabase.rawQuery(getUserInfo,null);

            int count = cu.getCount();
            System.out.println("countcountcountcountcountcount" + count);
            if (count > 0) {
                cu.moveToNext();
                CId = cu.getString(0);
                CPassword = cu.getString(1);

                System.out.println("CIdCIdCIdCIdCId" + CId);
                System.out.println("CPasswordCPassword" + CPassword);

                if(Id.equals(CId) && Password.equals(CPassword)){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "아이디 및 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "조회중 에러가 발생하였습니다.");
            Log.e(TAG, "error", e);
        }
    }

    //회원가입 이벤트
    public void mvLogin(View v) throws Exception{

        //회원가입 EditText
        EditText join_email = findViewById(R.id.joinEmail);
        EditText join_password = findViewById(R.id.joinPw);
        EditText join_name = findViewById(R.id.joinName);
        EditText join_id = findViewById(R.id.joinId);

        String UserEmail = join_email.getText().toString();
        String UserPwd = join_password.getText().toString();
        String UserName = join_name.getText().toString();
        String UserId = join_id.getText().toString();

        //유효성 검사
        AlertDialog dialog;

        if(UserName.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder.setMessage("이름을 입력해주세요.").setNegativeButton("확인", null).create();
            dialog.show();
            Toast.makeText(getApplicationContext(), "아이디 및 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();

            return;
        }
        else if (UserEmail.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder.setMessage("이메일을 입력해주세요.").setNegativeButton("확인", null).create();
            dialog.show();

            return;
        }
        else if(UserId.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder.setMessage("아이디를 입력해주세요.").setNegativeButton("확인", null).create();
            dialog.show();

            return;
        }
        else if(UserPwd.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            dialog = builder.setMessage("비밀번호를 입력해주세요.").setNegativeButton("확인", null).create();
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
                long result = sqliteDatabase.insert("TB_USERS",null, values);

                //성공 시 TB_USERS 조회
                if(result > 0) {
                    //회원가입 성공 팝업
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("회원가입이 완료되었습니다.").setNegativeButton("확인", null).create();
                    dialog.show();

                    //로그인화면으로 이동
                    backLogin(v);
                }
            }catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "데이터 입력중 에러가 발생하였습니다");
            }
        }
    }
}