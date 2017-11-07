package com.hosiluan.simplecamera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ho Si Luan on 11/7/2017.
 */

public class BaseActivity extends AppCompatActivity {


    private static final int MY_PERMISSION_REQUEST = 1;
    private static final int PERMISSION_CAMERA = 1;
    private static final int PERMISSION_WRITE_STORAGE = 2;
    private static final int PERMISSION_READ_STORAGE = 3;

//    protected void initPermission() {
//
//        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        ArrayList<String> neededPermissionList = new ArrayList<>();
//        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
//            neededPermissionList.add(Manifest.permission.CAMERA);
//        }
//        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
//            neededPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
//            neededPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//
//        if (neededPermissionList.size() > 0) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(neededPermissionList.toArray(new String[neededPermissionList.size()]),
//                        MY_PERMISSION_REQUEST);
//            }
//        }
//    }

    protected void checkCameraPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            }
        }
    }

    protected void checkWriteStoragePermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_STORAGE);
            }
        }
    }

    protected void checkReadtoragePermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CAMERA:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "camera ok", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PERMISSION_WRITE_STORAGE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "write storage ok", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PERMISSION_READ_STORAGE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "read storage ok", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
