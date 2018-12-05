package com.project.pfc.platformcleansing;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BunkerDBHelper bunkerDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bunkerDBHelper = new BunkerDBHelper(this);     // sqlite DB 생성

        File database = getApplicationContext().getDatabasePath(BunkerContract.DB_NAME);
        if(false == database.exists()){
            bunkerDBHelper.getReadableDatabase();

            if(copyDatabase(this)){
                Toast.makeText(this, "데이터를 불러오는데 성공했습니다." ,Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
            finish();
        }

        MainFragment mainFragment = new MainFragment();
        mainFragment.setIndex(getIntent().getIntExtra("index", -1));
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, mainFragment).commit();
    }

    private boolean copyDatabase(Context context){                                      // assets 폴더에 미리 넣어놓은 데이터 베이스 복사
        try{
            InputStream inputStream = context.getAssets().open(BunkerContract.DB_NAME);
            String outFileName = bunkerDBHelper.DBLOCATION + BunkerContract.DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte [] buff = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buff)) >0){
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   //액션바 추가
        MenuInflater inflater = getMenuInflater();    //메뉴 인플레이터 정보 얻기
        inflater.inflate(R.menu.main_menu, menu);     //커스텀 메뉴 적용
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){        //메뉴 아이템 이벤트 처리
        switch (item.getItemId()){
            case R.id.action_add:                                                               //추가 버튼 클릭 시 EditActivity 호출
                Intent goToEdit = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(goToEdit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
