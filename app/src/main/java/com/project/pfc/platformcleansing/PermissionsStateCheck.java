package com.project.pfc.platformcleansing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public final class PermissionsStateCheck {            // 권한 확인
    public static final String [] permissions= {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int permission_location = 0;
    public static final int permission_read_external = 1;
    public static final int permission_write_external = 2;

    public static boolean permissionState(Context context, int permission_index){                  //받아온 권한 상태 반환
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context,permissions[permission_index]);
    }

    public static void setPermissions(Activity activity, int permission_index, int requestCode){   //받아온 권한 승인 요청
        ActivityCompat.requestPermissions(activity, new String[] {permissions[permission_index]}, requestCode);
    }

    public static void setAllPermissions(Activity activity, int requestCode){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

}
