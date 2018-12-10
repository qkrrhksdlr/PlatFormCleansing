package com.project.pfc.platformcleansing;


import android.content.SharedPreferences;
import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCES_ID = "ID";
    public static final String PREFERENCES_PWD = "PASS";

    public static boolean LoginFlag = false;
    public static String LoginID = null;

    SharedPreferences setting;

    private BunkerDBHelper dbHelper;

    Button btn_SignIn;
    Button btn_SignUp;
    EditText editID;
    EditText editPASS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setting = getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE);

        dbHelper = new BunkerDBHelper(this);
        btn_SignIn = (Button) findViewById(R.id.btn_signin);
        btn_SignUp = (Button) findViewById(R.id.btn_signup);
        editID = (EditText) findViewById(R.id.user_id);
        editPASS = (EditText) findViewById(R.id.user_pwd);

        getIDPass();

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputID = editID.getText().toString();
                String inputPass = editPASS.getText().toString();
                String IDdata = "";
                String PASSWORDdata = "";
                Cursor cursor = dbHelper.getUserData(inputID);
                if(cursor.moveToNext()) {
                    IDdata = cursor.getString(0);
                    PASSWORDdata = cursor.getString(1);
                }
                saveID(inputID);
                savePWD(inputPass);

                if(inputID.equals(IDdata) && inputPass.equals(PASSWORDdata)){
                    LoginFlag = true;
                    LoginID = IDdata;
                    Toast.makeText(getApplicationContext(), "LOGIN SUCCESSFUL", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 잘못입력하셨습니다", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String inputID = editID.getText().toString();
                String inputPass = editPASS.getText().toString();
                dbHelper.insertUserData(inputID, inputPass);
                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다", Toast.LENGTH_LONG).show();// 가입하기 버튼 누르면 바로 가입
            }
        });

    }

    private void saveID(String text){
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(PREFERENCES_ID, text);
        editor.commit();
    }
    private void savePWD(String text) {
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(PREFERENCES_PWD, text);
        editor.commit();
    }
    private void getIDPass(){
        setting.getString(PREFERENCES_ID, "");
        setting.getString(PREFERENCES_PWD, "");
    }
}