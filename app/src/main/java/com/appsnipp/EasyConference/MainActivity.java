package com.appsnipp.EasyConference;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    private ListView conferenceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        };

        Button btnWrite = (Button) findViewById(R.id.addNew);
        btnWrite.setOnClickListener(listener);

        //리스트뷰 선언
        conferenceListView = (ListView) findViewById(R.id.conferenceListView);

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

        //리스트 뷰
        ListView lvList = (ListView)findViewById(R.id.conferenceListView);

        //리스트 출력
        selectListView();

        //리스트 뷰 선택 이벤트
        conferenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //수정화면으로 이동
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                int idx = 0;
                String Cnf_content = null;
                String Cnf_regdate = null;
                String Cnf_attendants = null;
                String Cnf_location = null;
                String Cnf_title = null;

                cu = sqliteDatabase.rawQuery("SELECT * FROM TB_CONFERENCE",null);

                if (cu.getCount() > 0) {
                    while (cu.moveToNext()) {
                        idx = cu.getInt(0);
                        Cnf_content = cu.getString(1);
                        Cnf_regdate = cu.getString(3);
                        Cnf_attendants = cu.getString(5);
                        Cnf_location = cu.getString(7);
                        Cnf_title = cu.getString(6);
                    }
                }
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);

                //수정 화면으로 리스트뷰 값 전달
                intent.putExtra("id", idx);
                intent.putExtra("content", Cnf_content);
                intent.putExtra("regdate", Cnf_regdate);
                intent.putExtra("attendants", Cnf_attendants);
                intent.putExtra("location", Cnf_location);
                intent.putExtra("title", Cnf_title);

                startActivity(intent);
            }
        });
    }

    //리스트 출력
    public void selectListView() {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        DateBaseHelper helper = new DateBaseHelper(MainActivity.this, "Conference.db", null, 1);
        sqliteDatabase = helper.getReadableDatabase();

        //리스트뷰에 목록 채워주는 도구 준비
        ListViewAdapter adapter = new ListViewAdapter();

        //Cursor라는 그릇에 목록을 담아주기
        cu = sqliteDatabase.rawQuery("SELECT _id, Cnf_title, Cnf_regdate FROM TB_CONFERENCE",null);

        if (cu.getCount() > 0) {
            //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
            while(cu.moveToNext()){
                adapter.addItemToList(cu.getInt(0), cu.getString(1),cu.getString(2));
            }
        }
        else{
            adapter.addItemToList(0,"조회된 리스트가 없습니다.","");
        }
        //리스트 표출
        conferenceListView.setAdapter(adapter);
    }

    //뒤로가기
    public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }
}
