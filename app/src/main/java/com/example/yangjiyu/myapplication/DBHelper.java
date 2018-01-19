package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangjiyu on 2018/1/15.
 * 使用可视化工具SQLite Expert Personal 3
 */

public class DBHelper extends SQLiteOpenHelper {
    private int m_type;
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void setM_type(int type){
        this.m_type=type;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (m_type==1){
            db.execSQL("CREATE TABLE IF NOT EXISTS alarm(" +
                    "id integer primary key autoincrement," +
                    "time varchar(10)," +
                    "cube varchar(5)," +
                    "des varchar(128)" +
                    ")");
        }else {
            db.execSQL("CREATE TABLE IF NOT EXISTS event(" +
                    "id integer primary key," +
                    "time varchar(10)," +
                    "cube varchar(5)," +
                    "des varchar(128)" +
                    ")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
