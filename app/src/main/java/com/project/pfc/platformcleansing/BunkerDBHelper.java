package com.project.pfc.platformcleansing;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class BunkerDBHelper extends SQLiteOpenHelper {
    public BunkerDBHelper(Context context){
        super(context, BunkerContract.DB_NAME, null, BunkerContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BunkerContract.Bunkers.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BunkerContract.Bunkers.DELETE_TABLE);
        onCreate(db);
    }

    public void insertBunkerData (String name, String call, double latitude, double longitude,
                                  String address1, String address2, int capacity, String reMarks){  // editActivity 에서 받아온 벙커 데이터 삽입
        try{
            String sql = String.format(
                    "INSERT INTO %s VALUES ('%s' '%s' '%f' '%f' '%s' '%s' '%d' '%s' '%s' '%b')",
                    name, call, latitude, longitude, address1,
                    address2, capacity, getDate() ,reMarks, false
            );

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e){

        }
    }

    public void deleteBunkerData(String name){   // 벙커 삭제시 데이터베이스에서 삭제
        try{
            String sql = String.format(
                    "DELETE FROM %s WHERE %s = %s",
                    BunkerContract.Bunkers.TABLE_NAME,
                    BunkerContract.Bunkers.KEY_NAME,
                    name
            );

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e){

        }
    }

    public void updateBunkerData(String name, String call, double latitude, double longitude,
                                 String address1, String address2, int capacity, String reMarks){  // 이미있는 내용 수정시 데이터베이스 수정
        try{
            String sql = String.format(
                    "UPDATE %s SET %s = '%s', %s = '%s', %s = '%f', %s = '%f', %s = '%f', %s = '%s', %s = '%s', %s = '%d', %s = '%s'",
                    BunkerContract.Bunkers.TABLE_NAME, BunkerContract.Bunkers.KEY_NAME, name, BunkerContract.Bunkers.KEY_CALL, call,
                    BunkerContract.Bunkers.KEY_LATITUDE, latitude, BunkerContract.Bunkers.KEY_LONGITUDE, longitude,
                    BunkerContract.Bunkers.KEY_ADDRESS_1, address1, BunkerContract.Bunkers.KEY_ADDRESS_2, address2,
                    BunkerContract.Bunkers.KEY_CAPACITY, capacity, BunkerContract.Bunkers.KEY_DATE, getDate(),
                    BunkerContract.Bunkers.KEY_REMARKS, reMarks
            );

            getWritableDatabase().execSQL(sql);
        }catch (SQLException e){

        }
    }

    public static String getDate(){    //현재 날짜 반환
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        String date = year + "-" + month + "-" + day;

        return date;
    }

}
