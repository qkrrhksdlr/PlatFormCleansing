package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class DetailViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private BunkerDBHelper bunkerDBHelper;
    private Cursor cursor;
    private GoogleMap map;

    private int setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Intent intent = getIntent();
        bunkerDBHelper = new BunkerDBHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detail_map);
        mapFragment.getMapAsync(this);

        TextView detail_name = (TextView) findViewById(R.id.detail_name);
        TextView detail_call = (TextView) findViewById(R.id.detail_call);
        TextView detail_capacity = (TextView) findViewById(R.id.detail_capacity);
        TextView detail_address1 = (TextView) findViewById(R.id.detail_address1);
        TextView detail_address2 = (TextView) findViewById(R.id.detail_address2);
        TextView detail_remarks = (TextView) findViewById(R.id.detail_remarks);
        TextView detail_user = (TextView) findViewById(R.id.detail_user);
        TextView detail_date = (TextView) findViewById(R.id.detail_date);

        cursor = bunkerDBHelper.getDetailData(intent.getIntExtra("id", -1));

        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "데이터를 불러오는데에 실패했습니다.\n다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            finish();
        }

        cursor.moveToNext();
        detail_name.setText(cursor.getString(BunkerContract.CursorIndex.NAME));
        detail_call.setText(cursor.getString(BunkerContract.CursorIndex.CALL));
        detail_capacity.setText(cursor.getString(BunkerContract.CursorIndex.CAPACITY));
        detail_address1.setText(cursor.getString(BunkerContract.CursorIndex.RNADDRESS));
        detail_address2.setText(cursor.getString(BunkerContract.CursorIndex.ADDRESS));
        detail_remarks.setText(cursor.getString(BunkerContract.CursorIndex.REMAKRS));
        detail_user.setText(cursor.getString(BunkerContract.CursorIndex.User));
        detail_date.setText(cursor.getString(BunkerContract.CursorIndex.DATE));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        setting = cursor.getInt(BunkerContract.CursorIndex.Favoirte);
        MenuItem btn_favorite = menu.findItem(R.id.favorite);
        if(setting == 1){
            btn_favorite.setIcon(android.R.drawable.star_big_on);
        } else {
            btn_favorite.setIcon(android.R.drawable.star_big_off);
        }

        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite :
                try{
                        if(setting == 1)
                            setting = 0;
                        else
                            setting = 1;
                        String sql = String.format("UPDATE %s SET %s = %d WHERE %s = %s",
                                BunkerContract.Bunkers.TABLE_NAME, BunkerContract.Bunkers.KEY_FAVORITE,
                                setting, BunkerContract.Bunkers._ID, cursor.getInt(BunkerContract.CursorIndex._ID)
                        );
                        bunkerDBHelper.WriteDBtoString(sql);

                        if(setting == 1){
                            item.setIcon(android.R.drawable.star_big_on);
                            Toast.makeText(getApplicationContext(), "즐겨찾기목록에 추가하였습니다. ", Toast.LENGTH_SHORT).show();
                        } else{
                            item.setIcon(android.R.drawable.star_big_off);
                            Toast.makeText(getApplicationContext(), "즐겨찾기목록에서 삭제되었습니다. ", Toast.LENGTH_SHORT).show();
                        }
                } catch (SQLException e){
                    if(setting == 1)
                        setting = 0;
                    else
                        setting = 1;
                    Toast.makeText(getApplicationContext(), "실패했습니다!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
            case R.id.delete :
                try {
                    bunkerDBHelper.deleteBunkerData(cursor.getInt(BunkerContract.CursorIndex._ID));
                    Toast.makeText(getApplicationContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (SQLException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.edit_activity:
                startActivity(new Intent(getApplicationContext(), EditActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
