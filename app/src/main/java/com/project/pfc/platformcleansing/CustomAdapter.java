package com.project.pfc.platformcleansing;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        name.setText(data.get(position).name);

        TextView call= (TextView) convertView.findViewById(R.id.text_call);
        call.setText(data.get(position).call);

        TextView num = (TextView) convertView.findViewById(R.id.text_num);
        num.setText(data.get(position).num + "명");

        ImageButton favorite = (ImageButton) convertView.findViewById(R.id.btn_favorite);
        favorite.setFocusable(false);
        final String favoriteMsg = "즐겨찾기에 추가되었습니다.";

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, favoriteMsg, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}

class BunkerItem{                   //메인 리스트에 보여줄 항목
    int img;                        //이미지
    String name;                    //이름
    String call;                //장소
    int num;

    BunkerItem(int img, String name, String call, int num){
        this.img = img;
        this.name = name;
        this.call = call;
        this.num = num;
    }
}
