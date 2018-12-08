package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private BunkerDBHelper dbHelper;
    private Cursor cursor;
    private boolean editFlag;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;

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

        editFlag = getIntent().getBooleanExtra("edit", false);

        name = (EditText) findViewById(R.id.edit_name);
        call = (EditText) findViewById(R.id.edit_call);
        capacity = (EditText) findViewById(R.id.edit_capacity);
        address = (EditText) findViewById(R.id.edit_address);
        remarks = (EditText) findViewById(R.id.edit_remarks);

        if(editFlag){
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(editFlag){
            cursor.moveToFirst();
            String name = cursor.getString(BunkerContract.CursorIndex.NAME);
            double latitude = cursor.getDouble(BunkerContract.CursorIndex.LATITUDE);
            double longitude = cursor.getDouble(BunkerContract.CursorIndex.LONGITUDE);
            addMaker(latitude, longitude, name);
        } else {
            if(PermissionsStateCheck.permissionState(this, PermissionsStateCheck.permission_location)){
                getLastLocation();
            } else {
                PermissionsStateCheck.setPermissions(EditActivity.this, PermissionsStateCheck.permission_location, 1);
            }
        }
    }
    @SuppressWarnings("MissingPermission")
    private void getLastLocation(){
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
}
