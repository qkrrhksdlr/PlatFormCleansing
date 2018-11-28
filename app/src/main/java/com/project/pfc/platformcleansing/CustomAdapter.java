package com.project.pfc.platformcleansing;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context context;                                    //현재 어플 context
    private int res;                                            //리스트 뷰 항목 리소스 파일
    private ArrayList<BunkerItem> data;                         //데이터가 저장된 배열

    public CustomAdapter(Context context, int res, ArrayList<BunkerItem> data){
        this.context = context;
        this.res = res;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(res, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.bunker_image);
        img.setImageResource(data.get(position).img);

        TextView name = (TextView) convertView.findViewById(R.id.bunker_name);
        name.setText(data.get(position).name);

        TextView location = (TextView) convertView.findViewById(R.id.bunker_location);
        location.setText(data.get(position).location);

        return convertView;
    }
}

class BunkerItem{                   //메인 리스트에 보여줄 항목
    int img;                        //이미지
    String name;                    //이름
    String location;                //장소

    BunkerItem(int img, String name, String location){
        this.img = img;
        this.name = name;
        this.location = location;
    }
}
