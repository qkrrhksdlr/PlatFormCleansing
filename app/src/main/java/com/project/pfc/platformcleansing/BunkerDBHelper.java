package com.project.pfc.platformcleansing;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class BunkerDBHelper extends SQLiteOpenHelper {
    public static final String DBLOCATION = "/data/data/com.project.pfc.platformcleansing/databases/";
    private Context context;
    private SQLiteDatabase database;
    public BunkerDBHelper(Context context){
        super(context, BunkerContract.DB_NAME, null, BunkerContract.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(BunkerContract.Bunkers.CREATE_TABLE);
        db.execSQL(BunkerContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BunkerContract.Bunkers.DELETE_TABLE);
        db.execSQL(BunkerContract.Users.DELETE_TABLE);
        onCreate(db);
    }
    /*
    public void openDatabase(){
        String dbPath = context.getDatabasePath(BunkerContract.DB_NAME).getPath();
        if(database != null && database.isOpen()){
            return;
        }

        database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase(){
        if(database!=null){
            database.close();
        }
    }
    */

    /*
    * 벙커 데이터베이스 관리
     */
    public void insertBunkerData (String name, String call, double latitude, double longitude,
                                  String address, int capacity, String reMarks, String user) throws SQLException{  // editActivity 에서 받아온 벙커 데이터 삽입
            String sql = String.format(
                    "INSERT INTO %s VALUES (NULL, '%s', '%s', %f, %f, '%s', %d, '%s', '%s', %d, '%s', %d)",
                    BunkerContract.Bunkers.TABLE_NAME, name, call, latitude, longitude, address, capacity, getDate(), reMarks, 0, user, -1
            );

            getWritableDatabase().execSQL(sql);

    }

    public void deleteBunkerData(int _id) throws SQLException{   // 벙커 삭제시 데이터베이스에서 삭제
            String sql = String.format(
                    "DELETE FROM %s WHERE %s = %s",
                    BunkerContract.Bunkers.TABLE_NAME,
                    BunkerContract.Bunkers._ID,
                    _id
            );

            getWritableDatabase().execSQL(sql);
    }

    public void updateBunkerData(String name, String call, double latitude, double longitude,
                                 String address, int capacity, String reMarks, int _id) throws SQLException{  // 이미있는 내용 수정시 데이터베이스 수정
        String sql = String.format(
                "UPDATE %s SET %s = '%s', %s = '%s', %s = %f, %s = %f, %s = '%s', %s = %d, %s = '%s', %s = '%s' WHERE %s = %d",
                BunkerContract.Bunkers.TABLE_NAME, BunkerContract.Bunkers.KEY_NAME, name, BunkerContract.Bunkers.KEY_CALL, call,
                BunkerContract.Bunkers.KEY_LATITUDE, latitude, BunkerContract.Bunkers.KEY_LONGITUDE, longitude,
                BunkerContract.Bunkers.KEY_ADDRESS, address,
                BunkerContract.Bunkers.KEY_CAPACITY, capacity, BunkerContract.Bunkers.KEY_DATE, getDate(),
                BunkerContract.Bunkers.KEY_REMARKS, reMarks, BunkerContract.Bunkers._ID, _id
        );

        getWritableDatabase().execSQL(sql);
    }

    public ArrayList<BunkerItem> getListItemForDB(String string){   // 리스트뷰에 뿌려줄 아이템들만 모아서 받아오기
        String sql;
        switch (string) {
            case "전체":
                sql = String.format(
                        "SELECT %s, %s, %s, %s, %s, %s FROM %s",
                        BunkerContract.Bunkers.KEY_NAME, BunkerContract.Bunkers.KEY_CALL,
                        BunkerContract.Bunkers.KEY_ADDRESS,
                        BunkerContract.Bunkers.KEY_CAPACITY, BunkerContract.Bunkers.KEY_FAVORITE,
                        BunkerContract.Bunkers._ID, BunkerContract.Bunkers.TABLE_NAME
                );
                break;
            case "즐겨찾기":
                sql = String.format(
                        "SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s",
                        BunkerContract.Bunkers.KEY_NAME, BunkerContract.Bunkers.KEY_CALL,
                        BunkerContract.Bunkers.KEY_ADDRESS,
                        BunkerContract.Bunkers.KEY_CAPACITY, BunkerContract.Bunkers.KEY_FAVORITE,
                        BunkerContract.Bunkers._ID, BunkerContract.Bunkers.TABLE_NAME,
                        BunkerContract.Bunkers.KEY_FAVORITE, 1);
                break;
            default:
                sql = String.format("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s LIKE '%s%%'",
                        BunkerContract.Bunkers.KEY_NAME, BunkerContract.Bunkers.KEY_CALL,
                        BunkerContract.Bunkers.KEY_ADDRESS,
                        BunkerContract.Bunkers.KEY_CAPACITY, BunkerContract.Bunkers.KEY_FAVORITE,
                        BunkerContract.Bunkers._ID, BunkerContract.Bunkers.TABLE_NAME,
                        BunkerContract.Bunkers.KEY_ADDRESS, string
                );
                break;
        }



        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        ArrayList<BunkerItem> listData = new ArrayList<BunkerItem>();

        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            String call = cursor.getString(1);
            String address = cursor.getString(2);
            int capacity = cursor.getInt(3);
            int favorite = cursor.getInt(4);
            int _id = cursor.getInt(5);

            listData.add(new BunkerItem(name, call, address, capacity, favorite, _id));
        }

        return listData;
    }

    public Cursor getDetailData(int _id){         // 디테일뷰에 보여줄 한 튜플 받아오기
        Cursor cursor;
        if(_id == -1){
            cursor = null;
        } else {
            String sql = String.format(
                    "SELECT * FROM %s WHERE %s = %d", BunkerContract.Bunkers.TABLE_NAME,
                    BunkerContract.Bunkers._ID, _id
            );
            cursor = getReadableDatabase().rawQuery(sql, null);
        }
        return cursor;
    }

    public void WriteDBtoString(String sql) throws SQLException{
        getWritableDatabase().execSQL(sql);
    }


    /*
    * 유저 데이터베이스관리
     */

    public void insertUserData(String ID , String passWord){    //회원가입시
        try{
            String sql = String.format("INSERT INTO %s VALUES ('%s '%s')",
                    BunkerContract.Users.TABLE_NAME, ID, passWord);

            getWritableDatabase().execSQL(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteUserData(String ID){           //회원 탈퇴시
        try{
            String sql = String.format("DELETE FROM %s WHERE %s = %s",
                    BunkerContract.Users.TABLE_NAME, BunkerContract.Users._ID, ID);

            getWritableDatabase().execSQL(sql);
        }catch (SQLException e){

        }
    }

    public void updateUserData(String ID, String passWord){    // 비밀번호 변경
        try{
            String sql = String.format("UPDATE  %s SET %s = '%s' WHERE %s = %s",
            BunkerContract.Users.TABLE_NAME, BunkerContract.Users._PASS, passWord, BunkerContract.Users._ID, ID);
            getWritableDatabase().execSQL(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Cursor getUserData(String ID){
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s",
                BunkerContract.Users.TABLE_NAME, BunkerContract.Users._ID, ID);
        return getReadableDatabase().rawQuery(sql, null);
    }

    public String getDate(){    //현재 날짜 반환
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);

        String date = year + "-" + month + "-" + day;

        return date;
    }

}
