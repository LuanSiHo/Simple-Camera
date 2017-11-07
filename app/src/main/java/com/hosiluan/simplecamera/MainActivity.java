package com.hosiluan.simplecamera;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.IOException;

public class MainActivity extends BaseActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    private Button mSwitchCameraButton;
    private ImageButton mSwitchCameraImageButton, mChangeFlashImageButton;
    private int mCurrentCameraId = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCameraPermision();
        checkReadtoragePermision();
        checkWriteStoragePermision();


        setView();
        setEvent();

        // get the camera
//        getCamera();

        // displaying button image
//        toggleButtonImage();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // on pause turn off the flash
//        turnOffFlash();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // on stop release the camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void setView() {
//        mSwitchCameraButton = findViewById(R.id.btn_switch_camera);
        mSwitchCameraImageButton = findViewById(R.id.img_btn_switch_camera);
        mChangeFlashImageButton = findViewById(R.id.img_btn_switch_flash);
        mCamera = Camera.open();

        if (mCamera != null) {

            mCameraView = new CameraView(this, mCamera);
            FrameLayout frameLayout = findViewById(R.id.camera_view);
            frameLayout.addView(mCameraView);
        }
    }

    private void setEvent() {


        mSwitchCameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCameraView.switchCamera(mCurrentCameraId);
                if (mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCurrentCameraId = 1;
                } else {
                    mCurrentCameraId = 0;
                }
            }
        });

        mChangeFlashImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCameraView.changeFlashStatus();
                toggleButtonImage();
            }
        });
    }

    /*
    * Toggle switch button images
    * changing image states to on / off
    * */
    private void toggleButtonImage() {
        if (mCameraView.isFlashOn()) {
            mChangeFlashImageButton.setImageResource(R.drawable.ic_flash_off);
        } else {
            Log.d("Luan", "toggle");

            mChangeFlashImageButton.setImageResource(R.drawable.ic_flash_on);
        }
    }

}
