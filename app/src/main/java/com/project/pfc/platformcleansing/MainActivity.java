package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        ArrayList<BunkerItem> data = new ArrayList<BunkerItem>();    //데이터 저장 배열 생성
        data.add(new BunkerItem(R.drawable.bunker1, "테란벙커", "0101312312", 6));
        data.add(new BunkerItem(R.drawable.bunker2, "바다벙커", "01014143431",3));
        data.add(new BunkerItem(R.drawable.bunker3, "위장벙커", "01014134134",6));
        data.add(new BunkerItem(R.drawable.bunker4, "민간벙커", "031010410",5));
        data.add(new BunkerItem(R.drawable.bunker5, "Shelter", "02114134", 8));
        data.add(new BunkerItem(R.drawable.bunker6, "지하벙커", "02124142", 9));

        bunkerAdapter = new CustomAdapter(this, R.layout.item, data);  //어댑터 생성

        ListView bunkerList = findViewById(R.id.bunker_list);  //메인 리스트뷰
        bunkerList.setAdapter(bunkerAdapter);
        bunkerList.setDivider(new ColorDrawable(Color.BLACK));  //리스트뷰 구분자
        bunkerList.setDividerHeight(5);

        bunkerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentDetail = new Intent(getApplicationContext(), DetailViewActivity.class);
                String data_name = ((BunkerItem)bunkerAdapter.getItem(position)).name;                   //현재 선택된 벙커 이름
                intentDetail.putExtra("name", data_name);                                         //벙커이름을 DetailViewActivity로 보냄
                startActivity(intentDetail);
                Toast.makeText(MainActivity.this, R.string.msg, Toast.LENGTH_SHORT).show(); // 테스트용 토스트
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   //액션바 추가
        MenuInflater inflater = getMenuInflater();    //메뉴 인플레이터 정보 얻기
        inflater.inflate(R.menu.main_menu, menu);     //커스텀 메뉴 적용

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
