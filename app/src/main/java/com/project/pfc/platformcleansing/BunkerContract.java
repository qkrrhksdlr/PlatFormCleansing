package com.project.pfc.platformcleansing;

public final class BunkerContract {                    //대피소 관련정보, 사용자 정보가 들어갈 데이터 베이스
    public static final String DB_NAME="대피소.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUM_TYPE = " DECIMAL(11,8)";    // 경위도값 정확한 값이 들어가야 하므로 고정소숫점 자료형 사용
    private static final String COMMA = ",";

    private BunkerContract(){
    }

    public static class Bunkers{                        // 대피소현황을 저장할 테이블
        public static final String TABLE_NAME = "대피소현황";
        public static final String KEY_NAME = "대피소명";
        public static final String KEY_CALL = "전화번호";
        public static final String KEY_LATITUDE = "위도";
        public static final String KEY_LONGITUDE = "경도";
        public static final String KEY_ADDRESS_1 = "도로명주소";
        public static final String KEY_ADDRESS_2 = "지번주소";
        public static final String KEY_CAPACITY = "수용인원";
        public static final String KEY_DATE = "수정일자";
        public static final String KEY_REMARKS = "비고";
        public static final String KEY_FAVORIT = "즐겨찾기여부";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_NAME + TEXT_TYPE + " PRIMARY KEY" + COMMA + KEY_CALL + TEXT_TYPE + COMMA +
                KEY_LATITUDE + NUM_TYPE + COMMA + KEY_LONGITUDE + NUM_TYPE + COMMA +
                KEY_ADDRESS_1 + TEXT_TYPE + COMMA + KEY_ADDRESS_2 + TEXT_TYPE + COMMA +
                KEY_CAPACITY + " INTEGER" + COMMA + KEY_DATE + " DATE" + COMMA +
                KEY_REMARKS + TEXT_TYPE + COMMA + KEY_FAVORIT + " BOOL )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Users{        //사용자 아이디 비밀번호가 들어갈 테이블
        public static final String TABLE_NAME = "사용자";
        public static final String _ID = "id";
        public static final String _PASS = "password";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + TEXT_TYPE + " PRIMARY KEY" + COMMA + _PASS + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
