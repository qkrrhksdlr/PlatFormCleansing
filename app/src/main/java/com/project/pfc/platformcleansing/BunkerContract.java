package com.project.pfc.platformcleansing;

import android.provider.BaseColumns;

public final class BunkerContract {                    //대피소 관련정보, 사용자 정보가 들어갈 데이터 베이스
    public static final String DB_NAME="BunkerData.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";    // 경위도값 정확한 값이 들어가야 하므로 소숫점 자료형 사용
    private static final String COMMA = ",";

    private BunkerContract(){
    }

    public static class Bunkers implements BaseColumns {                        // 대피소현황을 저장할 테이블
        public static final String TABLE_NAME = "Bunkers";
        public static final String KEY_NAME = "Name";
        public static final String KEY_CALL = "Call";
        public static final String KEY_LATITUDE = "Latitude";
        public static final String KEY_LONGITUDE = "Longitude";
        public static final String KEY_ADDRESS_1 = "RNAddress";
        public static final String KEY_ADDRESS_2 = "Address";
        public static final String KEY_CAPACITY = "Capacity";
        public static final String KEY_DATE = "Date";
        public static final String KEY_REMARKS = "Remarks";
        public static final String KEY_FAVORITE = "Favorite";
        public static final String KEY_USER = "User";
        public static final String KEY_IMAGE = "Image";
        
        /*
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + INT_TYPE  + " PRIMARY KEY" + COMMA +
                KEY_NAME + TEXT_TYPE + COMMA + KEY_CALL + TEXT_TYPE + COMMA +
                KEY_LATITUDE + REAL_TYPE + COMMA + KEY_LONGITUDE + REAL_TYPE + COMMA +
                KEY_ADDRESS_1 + TEXT_TYPE + COMMA + KEY_ADDRESS_2 + TEXT_TYPE + COMMA +
                KEY_CAPACITY + INT_TYPE + COMMA + KEY_DATE + TEXT_TYPE + COMMA +
                KEY_REMARKS + TEXT_TYPE + COMMA + KEY_FAVORITE + INT_TYPE + " DEFAULT 0" + COMMA +
                KEY_USER + TEXT_TYPE + COMMA + KEY_IMAGE + INT_TYPE + " DEFAULT -1" +
                ")";
        */

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Users implements BaseColumns {        //사용자 아이디 비밀번호가 들어갈 테이블
        public static final String TABLE_NAME = "사용자";
        public static final String _ID = "id";
        public static final String _PASS = "password";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + TEXT_TYPE + " PRIMARY KEY" + COMMA + _PASS + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
    public static class CursorIndex{
        public static final int _ID = 0;
        public static final int NAME = 1;
        public static final int CALL = 2;
        public static final int LATITUDE = 3;
        public static final int LONGITUDE = 4;
        public static final int RNADDRESS = 5;
        public static final int ADDRESS = 6;
        public static final int CAPACITY = 7;
        public static final int DATE = 8;
        public static final int REMAKRS = 9;
        public static final int Favoirte = 10;
        public static final int User = 11;
        public static final int Image = 12;
    }
}
