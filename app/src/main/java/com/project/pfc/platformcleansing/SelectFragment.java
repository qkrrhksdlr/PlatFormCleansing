package com.project.pfc.platformcleansing;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFragment extends Fragment {
    private int checkPosition = -1;
    public static final String [] list = {"전체", "즐겨찾기", "서울특별시", "광주광역시", "대구광역시", "대전광역시", "부산광역시",
            "울산광역시", "인천광역시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도"};

    public SelectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_select, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkPosition = position;
                Activity activity = getActivity();
                String name = list[checkPosition];
                if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("SelectedName", name);
                    startActivity(intent);
                } else {
                    MainFragment mainFragment = new MainFragment();
                    mainFragment.setSelection(name);
                }
            }
        });
        return rootView;
    }
/*
    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            checkPosition = savedInstanceState.getInt("check", -1);
            if(checkPosition >= 0){
                Activity activity = getActivity();
                String name = list[checkPosition];
                ((OnListSelectedListener)activity).onListSelected(name);

                ListView listView = (ListView) getView().findViewById(R.id.listView);
                listView.setSelection(checkPosition);
                listView.smoothScrollToPosition(checkPosition);
            }
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("check", checkPosition);
    }

}
