package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static CustomAdapter bunkerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<BunkerItem> data = new ArrayList<BunkerItem>();
        data.add(new BunkerItem(R.drawable.bunker1, "테란벙커", "마사라"));
        data.add(new BunkerItem(R.drawable.bunker2, "바다벙커", "해변"));
        data.add(new BunkerItem(R.drawable.bunker3, "위장벙커", "잔디밭"));
        data.add(new BunkerItem(R.drawable.bunker4, "민간벙커", "슈퍼앞"));
        data.add(new BunkerItem(R.drawable.bunker5, "Shelter", "에란겔"));
        data.add(new BunkerItem(R.drawable.bunker6, "지하벙커", "숲"));

        bunkerAdapter = new CustomAdapter(this, R.layout.item, data);

        ListView bunkerList = findViewById(R.id.bunker_list);  //메인 리스트뷰
        bunkerList.setAdapter(bunkerAdapter);

        bunkerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailViewActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, R.string.msg, Toast.LENGTH_SHORT).show(); // 테스트용 토스트
            }
        });
    }
}
