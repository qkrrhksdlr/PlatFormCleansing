package com.project.pfc.platformcleansing;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetailViewActivity extends AppCompatActivity {
    private BunkerDBHelper bunkerDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Intent intent = getIntent();
        bunkerDBHelper = new BunkerDBHelper(this);

        TextView detail_name = (TextView) findViewById(R.id.detail_name);
        TextView detail_call = (TextView) findViewById(R.id.detail_call);
        TextView detail_capacity = (TextView) findViewById(R.id.detail_capacity);
        TextView detail_address1 = (TextView) findViewById(R.id.detail_address1);
        TextView detail_address2 = (TextView) findViewById(R.id.detail_address2);
        TextView detail_remarks = (TextView) findViewById(R.id.detail_remarks);
        TextView detail_user = (TextView) findViewById(R.id.detail_user);
        TextView detail_date = (TextView) findViewById(R.id.detail_date);

        Cursor cursor = bunkerDBHelper.getDetailData(intent.getIntExtra("id", -1));

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
        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_activity:
                startActivity(new Intent(getApplicationContext(), EditActivity.class));
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
