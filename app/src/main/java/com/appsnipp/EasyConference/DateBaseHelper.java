package com.appsnipp.EasyConference;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DateBaseHelper extends SQLiteOpenHelper {
    public DateBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userSql = "CREATE TABLE if not exists TB_USERS ("
                + "_id integer primary key autoincrement,"
                + "name text,"
                + "email text,"
                + "password text,"
                + "id text);";

        String conferenceSql = "CREATE TABLE if not exists TB_CONFERENCE ("
                + "_id integer primary key autoincrement,"
                + "Cnf_id text);";

        sqLiteDatabase.execSQL(userSql);
        sqLiteDatabase.execSQL(conferenceSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
