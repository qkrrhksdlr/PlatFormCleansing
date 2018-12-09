package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class LoginActivity extends AppCompatActivity {
    private BunkerDBHelper dbHelper;
    SQLiteDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDatabase = init_database();

        final TextView loginText = (TextView) findViewById(R.id.LOGIN_text);
        final Button btn_Signin = (Button) findViewById(R.id.btn_signin);
        final Button btn_Signup = (Button) findViewById(R.id.btn_signup);
        final EditText editID = (EditText) findViewById(R.id.user_id);
        final EditText editPASS = (EditText) findViewById(R.id.user_pwd);

        dbHelper = new BunkerDBHelper(this);

        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = editID.getText().toString();
                Cursor cursor = dbHelper.getUserData(ID);
                cursor.moveToFirst();
                String PASSWORD = editPASS.getText().toString();
                cursor = dbHelper.getUserData(PASSWORD);
                String IDdata = cursor.getString(0);
                String PASSWORDdata = cursor.getString(1);

                if(editID.equals(IDdata) && editPASS.equals(PASSWORDdata)){
                    Toast.makeText(getApplicationContext(), "로그인성공춘", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 잘못입력하셨습니다", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_Signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //회원가입 누르면 LOGIN 텍스트와 아디,비번창 + 버튼도 가입하기 로 바꿀수있게이 회원가입 형식으로 바뀌게
                //id : LOGIN_text ,  ,
                loginText.setText("회 원 가 입");
                btn_Signup.setText("가입하기");
                btn_Signin.setEnabled(false);
            }
        });
    }

    private SQLiteDatabase init_database() {

        SQLiteDatabase db = null;
        File userfile = new File(getFilesDir(), "UserDatabase");
        try{
            db = SQLiteDatabase.openOrCreateDatabase(userfile, null);
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(db==null){
            Toast.makeText(this, "데이터 베이스 불러오기 실패", Toast.LENGTH_SHORT).show();
        }

        return db;
    }
}
