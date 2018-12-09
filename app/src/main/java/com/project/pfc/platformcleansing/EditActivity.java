package com.project.pfc.platformcleansing;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;                      //맵
    private BunkerDBHelper dbHelper;           //데이터베이스
    private Cursor cursor;                      //커서
    private boolean editFlag;                   //true면 수정, false면 추가
    private FusedLocationProviderClient fusedLocationProviderClient;  //추가시 현재위치 받아오기 위함
    private Location lastLocation;                                      //위치 저장할 곳

    EditText name;
    EditText call;
    EditText capacity;
    EditText address;
    EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.edit_map);
        mapFragment.getMapAsync(this);

        dbHelper = new BunkerDBHelper(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        editFlag = getIntent().getBooleanExtra("edit", false);                  //추가or수정 모드 확인

        name = (EditText) findViewById(R.id.edit_name);
        call = (EditText) findViewById(R.id.edit_call);
        capacity = (EditText) findViewById(R.id.edit_capacity);
        address = (EditText) findViewById(R.id.edit_address);
        remarks = (EditText) findViewById(R.id.edit_remarks);
        ImageButton imageButton = findViewById(R.id.edit_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();                             //image버튼 클릭시 사진 변경 or 추가
            }
        });

        if(editFlag){                                    //수정 모드 일 시 database에서 선택한 data 가져와서 값 입력
            int _id = getIntent().getIntExtra("id", -1);
            if(_id > 0) {
                cursor = dbHelper.getDetailData(_id);
                while(cursor.moveToNext()){
                    name.setText(cursor.getString(BunkerContract.CursorIndex.NAME));
                    call.setText(cursor.getString(BunkerContract.CursorIndex.CALL));
                    capacity.setText(cursor.getString(BunkerContract.CursorIndex.CAPACITY));
                    address.setText(cursor.getString(BunkerContract.CursorIndex.ADDRESS));
                    remarks.setText(cursor.getString(BunkerContract.CursorIndex.REMAKRS));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_save:
                saveDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        saveDialog();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(editFlag){           //수정 모드일 시 데이터베이스에서 넘어온 위도 경도로 카메라 이동 + 마커찍기
            cursor.moveToFirst();
            String name = cursor.getString(BunkerContract.CursorIndex.NAME);
            double latitude = cursor.getDouble(BunkerContract.CursorIndex.LATITUDE);
            double longitude = cursor.getDouble(BunkerContract.CursorIndex.LONGITUDE);
            addMaker(latitude, longitude, name);
        } else {                //추가 모드일 시 권한 확인 후 현재 위치로 이동 + 마커 찍기
            if(PermissionsStateCheck.permissionState(this, PermissionsStateCheck.permission_location)){
                getLastLocation();
            } else {
                PermissionsStateCheck.setPermissions(EditActivity.this, PermissionsStateCheck.permission_location, 1);
            }
        }
    }
    @SuppressWarnings("MissingPermission")
    private void getLastLocation(){                         //현재 위치 반환
        Task task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    lastLocation = location;
                    addMaker(lastLocation.getLatitude(), lastLocation.getLongitude(), "현재위치");
                } else {
                    Toast.makeText(getApplicationContext(), "위치정보를 얻어오는데 실패했습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getLastLocation();
        }
    }

    public void addMaker(double latitude, double longitude, String name){
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        map.addMarker(new MarkerOptions().alpha(0.8f).title(name).position(latLng));
    }
    public void show(){                         //이미지 버튼 클릭 시 사진을 갤러리 or 카메라에서 가져올 것 선택하는 부분
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("갤러리에서 가져오기");
        ListItems.add("카메라로 사진 찍기");

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진 추가하기");
        builder.setSingleChoiceItems(items, defaultItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SelectedItems.clear();
                SelectedItems.add(which);
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!SelectedItems.isEmpty()){
                    int index = (int) SelectedItems.get(0);
                    if(index == 0){

                    } else if (index == 1) {

                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    public void saveDialog(){                     //delete 버튼 클릭시 안내창
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save");
        builder.setMessage("저장하시겠습니까?");
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {          //예  눌렀을 시
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameText = name.getText().toString();
                String callText = call.getText().toString();
                int capacityText = Integer.parseInt(capacity.getText().toString());

            }
        });
        builder.setNegativeButton("저장하지 않음", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditActivity.super.onBackPressed();
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

}
