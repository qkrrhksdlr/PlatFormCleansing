package com.project.pfc.platformcleansing;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BunkerAdapter extends BaseAdapter {
    private Context context;                                    //현재 어플 context
    private int res;                                            //리스트 뷰 항목 리소스 파일
    private ArrayList<BunkerItem> data;                         //데이터가 저장된 배열

    public BunkerAdapter(Context context, int res, ArrayList<BunkerItem> data){
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
        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        TextView call = (TextView) convertView.findViewById(R.id.text_call);
        TextView capacity = (TextView) convertView.findViewById(R.id.text_capacity);
        TextView address1 = (TextView) convertView.findViewById(R.id.text_address1);
        TextView address2 = (TextView) convertView.findViewById(R.id.text_address2);
        ImageButton favorite = (ImageButton) convertView.findViewById(R.id.btn_favorite);

        name.setText(((BunkerItem)getItem(position)).name);
        call.setText(((BunkerItem)getItem(position)).call);
        capacity.setText(Integer.toString(((BunkerItem)getItem(position)).capacity));
        address1.setText(((BunkerItem)getItem(position)).address1);
        address2.setText(((BunkerItem)getItem(position)).address2);

        if(((BunkerItem)getItem(position)).favorite)
            favorite.setImageResource(android.R.drawable.star_on);
        else
            favorite.setImageResource(android.R.drawable.star_off);

        return convertView;
    }
}

class BunkerItem{                   //메인 리스트에 보여줄 항목
    public String name;        //대피소명
    public String call;        //전화번호
    public String address1;     //도로명주소
    public String address2;     //지번주소
    public int capacity;        //수용인원
    public boolean favorite;    //즐겨찾기여부

    public BunkerItem(String name, String call, String address1, String address2, int capacity, int favorite) {
        this.name = name;
        this.call = call;
        this.address1 = address1;
        this.address2 = address2;
        this.capacity = capacity;
        if(favorite == 1)
            this.favorite = true;
        else
            this.favorite = false;
    }
}
