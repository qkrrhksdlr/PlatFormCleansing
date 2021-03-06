package com.project.pfc.platformcleansing;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private BunkerDBHelper bunkerDBHelper;     //데이터베이스 활용
    private Cursor cursor;                      //데이터 받을 커서
    private GoogleMap map;                      //맵
    private int setting;                        //즐겨찾기여부
    private MenuItem loginItem;                 //로그인 버튼 저장소

    TextView detail_name;                      //위젯 받아오기
    TextView detail_call;
    TextView detail_capacity;
    TextView detail_address;
    TextView detail_remarks;
    TextView detail_user;
    TextView detail_date;
    ImageView detail_image;
    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        bunkerDBHelper = new BunkerDBHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detail_map);  //지도 연결
        mapFragment.getMapAsync(this);

        detail_name = (TextView) findViewById(R.id.detail_name);                       //위젯 받아오기
        detail_call = (TextView) findViewById(R.id.detail_call);
        detail_capacity = (TextView) findViewById(R.id.detail_capacity);
        detail_address = (TextView) findViewById(R.id.detail_address);
        detail_remarks = (TextView) findViewById(R.id.detail_remarks);
        detail_user = (TextView) findViewById(R.id.detail_user);
        detail_date = (TextView) findViewById(R.id.detail_date);
        detail_image = (ImageView) findViewById(R.id.detail_image);

    }

    @Override
    protected void onResume() {     //다시 돌아왔을때 데이터 갱신 위함
        super.onResume();
        cursor = bunkerDBHelper.getDetailData(getIntent().getIntExtra("id", -1));
        if (cursor == null) {                   //데이터 받아오기 실패 했을 때 메인으로 돌아감
            Toast.makeText(getApplicationContext(), R.string.data_not_find, Toast.LENGTH_SHORT).show();
            finish();
        }

        cursor.moveToNext();                            //항목에 데이터 입력
        setTitle(cursor.getString(BunkerContract.CursorIndex.NAME)); // 타이틀바 대피소 목록
        detail_name.setText(cursor.getString(BunkerContract.CursorIndex.NAME));
        detail_call.setText(cursor.getString(BunkerContract.CursorIndex.CALL));
        detail_capacity.setText(cursor.getString(BunkerContract.CursorIndex.CAPACITY));
        detail_address.setText(cursor.getString(BunkerContract.CursorIndex.ADDRESS));
        detail_remarks.setText(cursor.getString(BunkerContract.CursorIndex.REMAKRS));
        detail_user.setText(cursor.getString(BunkerContract.CursorIndex.User));
        detail_date.setText(cursor.getString(BunkerContract.CursorIndex.DATE));
        detail_image.setImageURI(Uri.parse("file://" + cursor.getString(BunkerContract.CursorIndex.Image)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        setting = cursor.getInt(BunkerContract.CursorIndex.Favoirte);           //즐겨 찾기 여부에 따른 버튼 모양 변경
        MenuItem btn_favorite = menu.findItem(R.id.favorite);
        if(setting == 1){
            btn_favorite.setIcon(android.R.drawable.star_big_on);
        } else {
            btn_favorite.setIcon(android.R.drawable.star_big_off);
        }
        loginItem = menu.findItem(R.id.detail_loginButton);     //로그인 아이템 넣음

        return  super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){         //로그인 버튼 실시간 변경
        MenuItem loginItem = menu.findItem(R.id.detail_loginButton);
        if(LoginActivity.LoginFlag){
            loginItem.setTitle(R.string.logout);
        } else {
            loginItem.setTitle(R.string.login);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call :                //벙커에 저장된 전화번호로 전화걸기
                cursor.moveToFirst();
                String call = cursor.getString(BunkerContract.CursorIndex.CALL);
                Intent implicit_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + call)); // 전화 인텐트 시작
                startActivity(implicit_intent);
                return true;
            case R.id.favorite :                    //즐겨찾기 버튼 누를시 등록, 취소 변경
                try{
                        if(setting == 1)
                            setting = 0;
                        else
                            setting = 1;
                        String sql = String.format("UPDATE %s SET %s = %d WHERE %s = %d",
                                BunkerContract.Bunkers.TABLE_NAME, BunkerContract.Bunkers.KEY_FAVORITE,
                                setting, BunkerContract.Bunkers._ID, cursor.getInt(BunkerContract.CursorIndex._ID)
                        );
                        bunkerDBHelper.getWritableDatabase().execSQL(sql);              //변경한 값을 DB에 반영 실패시 catch 로 이동하고 버튼 변경 X

                        if(setting == 1){
                            item.setIcon(android.R.drawable.star_big_on);
                            Toast.makeText(getApplicationContext(), R.string.favorit_on_toast, Toast.LENGTH_SHORT).show();
                        } else{
                            item.setIcon(android.R.drawable.star_big_off);
                            Toast.makeText(getApplicationContext(), R.string.favorit_off_toast, Toast.LENGTH_SHORT).show();
                        }
                } catch (SQLException e){
                    if(setting == 1)
                        setting = 0;
                    else
                        setting = 1;
                    Toast.makeText(getApplicationContext(), R.string.favorit_fail_toast, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
            case R.id.detail_loginButton :          //로그인 버튼 이벤트
                if(LoginActivity.LoginFlag){
                    logoutDialog();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.delete :      //삭제 버튼 이벤트
                if(LoginActivity.LoginFlag) {       //로그인여부확인
                    deleteDialog();  //삭제 확인 안내창 띄움
                } else {
                    Toast.makeText(getApplicationContext(), R.string.request_login, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.edit_activity:
                if(LoginActivity.LoginFlag) {       //로그인 여부 확인
                    Intent goToEdit = new Intent(getApplicationContext(), EditActivity.class);      //editActivity 로 이동
                    goToEdit.putExtra("edit", true);                                    // 수정상태를 의미
                    goToEdit.putExtra("id", cursor.getInt(BunkerContract.CursorIndex._ID));    //현제 데이터 보여주기 위한 ID
                    startActivity(goToEdit);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.request_login, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteDialog(){                     //delete 버튼 클릭시 안내창
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {          //예  눌렀을 시
            @Override
            public void onClick(DialogInterface dialog, int which) {                //예 버튼 누를시
                try {
                    bunkerDBHelper.deleteBunkerData(cursor.getInt(BunkerContract.CursorIndex._ID));
                    Toast.makeText(getApplicationContext(), R.string.delete_success, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (SQLException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.delete_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {            //아니오 버튼 누를시
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    public void logoutDialog(){                 //로그아웃 확인 다이얼로그
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("정말 로그아웃 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {          //예  눌렀을 시
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.LoginID = null;
                LoginActivity.LoginFlag = false;
                Toast.makeText(getApplicationContext(), R.string.logout_msg, Toast.LENGTH_SHORT).show();
                loginItem.setTitle(R.string.login);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {           //받아온 위도 경도 값으로 지도 위치 이동
        map = googleMap;
        cursor.moveToFirst();
        String name = cursor.getString(BunkerContract.CursorIndex.NAME);
        double latitude = cursor.getDouble(BunkerContract.CursorIndex.LATITUDE);
        double longitude = cursor.getDouble(BunkerContract.CursorIndex.LONGITUDE);
        addMaker(latitude, longitude, name);
    }
    public void addMaker(double latitude, double longitude, String name){           //마커 추가
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        map.addMarker(new MarkerOptions().alpha(0.8f).title(name).position(latLng));
    }
}
