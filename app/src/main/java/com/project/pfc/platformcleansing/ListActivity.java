package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListActivity extends AppCompatActivity implements SelectFragment.OnTitleSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(R.string.main_app_name);
    }

    @Override
    public void onTitleSelected(int i) {
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
