package com.project.pfc.platformcleansing;

import android.content.Context;
import android.database.SQLException;
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
    private ArrayList<BunkerItem> data;                         //데이터가 저장된 배열
    private BunkerDBHelper database;

    public BunkerAdapter(Context context, ArrayList<BunkerItem> data, BunkerDBHelper database){
        this.context = context;
        this.data = data;
        this.database = database;
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
        final int pos = position;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.text_name);
        TextView call = (TextView) convertView.findViewById(R.id.text_call);
        TextView capacity = (TextView) convertView.findViewById(R.id.text_capacity);
        TextView address1 = (TextView) convertView.findViewById(R.id.text_address1);
        TextView address2 = (TextView) convertView.findViewById(R.id.text_address2);
        ImageButton favorite = (ImageButton) convertView.findViewById(R.id.btn_favorite);               //listView 아이템

        BunkerItem item = (BunkerItem)getItem(position);

        name.setText(item.name);
        call.setText(item.call);
        capacity.setText(Integer.toString(item.capacity));                 //텍스트 설정
        address1.setText(item.address1);
        address2.setText(item.address2);

        final int star_on = android.R.drawable.star_on;
        final int star_off = android.R.drawable.star_off;

        if(item.favorite == 1)                                                    //즐겨찾기 버튼 상태 변경
            favorite.setImageResource(star_on);
        else
            favorite.setImageResource(star_off);

        favorite.setFocusable(false);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{                                              // DB 업로드 실패시 버튼 이미지 변경 안하기 위한 구조
                    ImageButton favorite = (ImageButton) v;       // 클릭한 버튼
                    BunkerItem item = ((BunkerItem)getItem(pos));   //벙커아이템 받아옴
                    int setting = item.favorite;                   // 즐겨찾기 여부 저장

                    if(setting == 1)
                        setting = 0;
                    else
                        setting = 1;
                    String sql = String.format("UPDATE %s SET %s = %d WHERE %s = '%s'",
                            BunkerContract.Bunkers.TABLE_NAME, BunkerContract.Bunkers.KEY_FAVORITE,
                            setting, BunkerContract.Bunkers.KEY_NAME, item.name
                    );
                    database.WriteDBtoString(sql);

                    if(setting == 1){
                        favorite.setImageResource(star_on);
                        Toast.makeText(context, "즐겨찾기목록에 추가하였습니다. ", Toast.LENGTH_SHORT).show();
                    } else{
                        favorite.setImageResource(star_off);
                        Toast.makeText(context, "즐겨찾기목록에서 삭제되었습니다. ", Toast.LENGTH_SHORT).show();
                    }

                    ((BunkerItem)getItem(pos)).favorite = setting;   //현재 리스트뷰에 favorite값 업데이트 해주기 위함
                } catch (SQLException e){
                    Toast.makeText(context, "실패했습니다!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }
}

class BunkerItem{                   //메인 리스트에 보여줄 항목
    public String name;        //대피소명
    public String call;        //전화번호
    public String address1;     //도로명주소
    public String address2;     //지번주소
    public int capacity;        //수용인원
    public int favorite;    //즐겨찾기여부
    public int _id;

    public BunkerItem(String name, String call, String address1, String address2, int capacity, int favorite, int _id) {
        this.name = name;
        this.call = call;
        this.address1 = address1;
        this.address2 = address2;
        this.capacity = capacity;
        this.favorite = favorite;
        this._id = _id;
    }
}
