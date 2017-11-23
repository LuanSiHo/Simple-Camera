package com.hosiluan.simplecamera;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Ho Si Luan on 11/7/2017.
 */

public class BaseActivity extends AppCompatActivity {

    PermissionAcceptedListener mPermissionAcceptedListener;
    TakePictureListener mTakepicPictureListener;
    private static final int MY_PERMISSION_REQUEST = 1;
    private static final int PERMISSION_CAMERA = 1;
    private static final int PERMISSION_WRITE_STORAGE = 2;
    private static final int PERMISSION_READ_STORAGE = 3;


    IntentFilter intentFilter = new IntentFilter("com.luan.TAKE_PHOTO");
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTakepicPictureListener.takephoto();
            Log.d("Luan", "i'm here");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_STORAGE);
            }
        }
    }

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_WRITE_STORAGE);
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
                        mPermissionAcceptedListener.onCameraAccepted();
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


    protected ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if (file.getName().endsWith(".jpg")) {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public void setPermissionListener(PermissionAcceptedListener mPermissionAcceptedListener) {
        this.mPermissionAcceptedListener = mPermissionAcceptedListener;
    }

    public interface PermissionAcceptedListener {
        void onCameraAccepted();
    }

    protected void setTakePictureListener(TakePictureListener mTakePictureListener) {
        this.mTakepicPictureListener = mTakePictureListener;
    }

    public interface TakePictureListener {
        void takephoto();
    }
}
