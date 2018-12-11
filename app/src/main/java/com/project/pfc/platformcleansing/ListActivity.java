package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListActivity extends AppCompatActivity implements SelectFragment.OnTitleSelectedListener{          //처음 나올 액티비티

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(R.string.main_app_name);
        PermissionsStateCheck.setAllPermissions(ListActivity.this, 0);
    }

    @Override
    public void onTitleSelected(int i) {                        //리스트에서 선택한 아이템 넘겨주기
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            MainFragment mainFragment = new MainFragment();
            mainFragment.setIndex(i);
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, mainFragment).commit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("index", i);
            startActivity(intent);
        }
    }
}
