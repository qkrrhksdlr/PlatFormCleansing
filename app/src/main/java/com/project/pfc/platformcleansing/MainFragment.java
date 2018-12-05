package com.project.pfc.platformcleansing;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    private BunkerAdapter bunkerAdapter;
    private BunkerDBHelper bunkerDBHelper;
    private View rootView;
    static String name = "전체";

    public MainFragment() {
        // Required empty public constructor
    }

    public void setSelection(String name){ this.name = name; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        bunkerDBHelper = new BunkerDBHelper(getActivity());

        viewAllList();
        return rootView;
    }
    private void viewAllList(){
        ArrayList<BunkerItem> data = bunkerDBHelper.getListItemForDB(name);    //DB에서 리스트뷰 관련 아이템만 받아옴

        bunkerAdapter = new BunkerAdapter(getActivity(), data, bunkerDBHelper);  //어댑터 생성

        ListView bunkerList = rootView.findViewById(R.id.bunker_list);  //메인 리스트뷰
        bunkerList.setAdapter(bunkerAdapter);
        bunkerList.setDivider(new ColorDrawable(Color.BLACK));  //리스트뷰 구분자
        bunkerList.setDividerHeight(5);

        bunkerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentDetail = new Intent(getActivity(), DetailViewActivity.class);
                int _id = ((BunkerItem)bunkerAdapter.getItem(position))._id;                   //현재 선택된 벙커 이름
                intentDetail.putExtra("id", _id);                                         //벙커이름을 DetailViewActivity로 보냄
                startActivity(intentDetail);
            }
        });
    }
}
