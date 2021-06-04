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
    private ListView conferenceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    //리스트 출력
    public void selectListView() {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        DateBaseHelper helper = new DateBaseHelper(MainActivity.this, "Conference.db", null, 1);
        sqliteDatabase = helper.getReadableDatabase();

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        ListViewAdapter adapter = new ListViewAdapter();

        //Cursor라는 그릇에 목록을 담아주기
        cu = sqliteDatabase.rawQuery("SELECT Cnf_regdate, Cnf_title FROM TB_CONFERENCE",null);

        int count = cu.getCount();
        if (count > 0) {

            //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
            while(cu.moveToNext()){
                System.out.println("조회됨");
                //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번
                adapter.addItemToList(cu.getString(0),cu.getString(1));
            }
        }
        else{
            System.out.println("조회안됨");
            adapter.addItemToList("","조회된 리스트가 없습니다.");
        }

        conferenceListView.setAdapter(adapter);
    }

    //뒤로가기
    public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }
}
