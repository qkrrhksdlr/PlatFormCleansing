package com.project.pfc.platformcleansing;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCES_ID = "ID";               //
    public static final String PREFERENCES_PWD = "PASS";

    public static boolean LoginFlag = false;                    //로그인 상태 확인 변수
    public static String LoginID = null;                        //로그인 성공시 아이디 저장

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

        setting = getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE);           // sharedpreferences 불러오기

        dbHelper = new BunkerDBHelper(this);
        btn_SignIn = (Button) findViewById(R.id.btn_signin);
        btn_SignUp = (Button) findViewById(R.id.btn_signup);
        editID = (EditText) findViewById(R.id.user_id);
        editPASS = (EditText) findViewById(R.id.user_pwd);

        getIDPass();

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                               //로그인 버튼 이벤트
                String inputID = editID.getText().toString();
                String inputPass = editPASS.getText().toString();
                String IDdata = "";
                String PASSWORDdata = "";
                Cursor cursor = dbHelper.getUserData(inputID);
                if(cursor.moveToNext()) {           //받아온 정보가 있을 경우 데이터 넣기
                    IDdata = cursor.getString(0);
                    PASSWORDdata = cursor.getString(1);
                }
                saveID(inputID);
                savePWD(inputPass);

                if(inputID.equals(IDdata) && inputPass.equals(PASSWORDdata)){           //로그인 성공
                    LoginFlag = true;
                    LoginID = IDdata;
                    Toast.makeText(getApplicationContext(), LoginID + getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signUpDialog();
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
        editID.setText(setting.getString(PREFERENCES_ID, ""));
        editPASS.setText(setting.getString(PREFERENCES_PWD, ""));
    }
    public void signUpDialog(){                             //회원가입 안내 dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");
        builder.setMessage("이 정보로 가입하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {          //예  눌렀을 시
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputID = editID.getText().toString();
                String inputPass = editPASS.getText().toString();
                boolean userCheck = dbHelper.insertUserData(inputID, inputPass);
                if(userCheck){
                    Toast.makeText(getApplicationContext(), R.string.signup_success, Toast.LENGTH_LONG).show();// 가입하기 버튼 누르면 바로 가입
                } else {
                    Toast.makeText(getApplicationContext(), R.string.signup_failed, Toast.LENGTH_LONG).show();// 가입하기 버튼 누르면 바로 가입
                }
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}