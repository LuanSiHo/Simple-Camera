package com.hosiluan.simplecamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements CameraView.TakePhotoListener,
        BaseActivity.PermissionAcceptedListener, BaseActivity.TakePictureListener {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    int mCountDownTimerIndex = 4;

    public ImageView mLastestThumnailImageView;
    private Button mSwitchCameraButton;
    private ImageButton mSwitchCameraImageButton, mChangeFlashImageButton,
            mTakePhotoImageButton, mZoomInImageButton, mZoomOutImageButton, mTimerImageButton;
    private TextView mTimerStatusTextView, mTimerTextView;
    private int mCurrentCameraId = 0;
    private SeekBar mSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCameraPermision();
        checkReadtoragePermision();
        checkWriteStoragePermision();

        setView();
        setEvent();


//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//
//
//        ArrayList<File> files = getListFiles(mediaStorageDir);
//        Log.d("Luan",files.size() + " list size");


    }

    @Override
    protected void onResume() {
        super.onResume();
        setPermissionListener(this);
        setTakePictureListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            mCamera = Camera.open();
            if (mCamera != null) {
                mCameraView = new CameraView(this, mCamera);
                FrameLayout frameLayout = findViewById(R.id.camera_view);
                frameLayout.addView(mCameraView);
                mCameraView.setListener(this);
                mCameraView.refreshCamera();
            }
        }


    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {
        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = reqWidth;
        int nHeight = reqHeight;

        float parentRatio = (float) reqHeight / reqWidth;

        nHeight = bHeight;
        nWidth = (int) (reqWidth * parentRatio);

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // on stop release the camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void setView() {
        mSwitchCameraImageButton = findViewById(R.id.img_btn_switch_camera);
        mChangeFlashImageButton = findViewById(R.id.img_btn_switch_flash);
        mTakePhotoImageButton = findViewById(R.id.img_btn_take_photo);
        mLastestThumnailImageView = findViewById(R.id.img_lastest_thumnail);
        mZoomOutImageButton = findViewById(R.id.img_btn_zoom_out);
        mZoomInImageButton = findViewById(R.id.img_btn_zoom_in);
        mTimerImageButton = findViewById(R.id.img_btn_timer);
        mTimerStatusTextView = findViewById(R.id.tv_timer);
        mTimerTextView = findViewById(R.id.textview_timer);

        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setProgress(50);
    }

    private void setEvent() {

        mLastestThumnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ListPhoToActivity.class));
            }
        });

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
                toggleButtonImage();
            }
        });

        mTakePhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCameraView.isTimerOn()) {
                    CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
                        @Override
                        public void onTick(long l) {

                            mTimerTextView.setText(--mCountDownTimerIndex + "");
                            Log.d("Luan", "tick " + mCountDownTimerIndex);
                        }

                        @Override
                        public void onFinish() {
                            mTimerTextView.setText("");
                            Log.d("Luan", "finish");
                            Intent intent = new Intent();
                            intent.setAction("com.luan.TAKE_PHOTO");
                            sendBroadcast(intent);

                        }
                    };
                    countDownTimer.start();
                } else {
                    mCameraView.takePhoto();
                }
            }
        });

        mZoomInImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.zoomIn();
            }
        });

        mZoomOutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.zoomOut();
            }
        });

        mTimerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView.isTimerOn()) {
                    mTimerStatusTextView.setText("off");
                    mCameraView.setTimerOn(false);
                } else {
                    mTimerStatusTextView.setText("on");
                    mCameraView.setTimerOn(true);
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("Luan", i + "");
                if (i > 50) {
                    mCameraView.changeBrightness(i / 10);
                } else {
                    mCameraView.changeBrightness(-10 + (i / 10));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("Luan", "start trackingtouch");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Luan", "stop trackingtouch");

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
            mCameraView.setFlashOn(false);
        } else {
            Log.d("Luan", "toggle");
            mCameraView.setFlashOn(true);
            mChangeFlashImageButton.setImageResource(R.drawable.ic_flash_on);
        }
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap = getScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight());
            mLastestThumnailImageView.setImageBitmap(bitmap);
            mLastestThumnailImageView.setRotation(90);
        }
    }

    @Override
    public void onCameraAccepted() {
        mCamera = Camera.open();

        if (mCamera != null) {

            mCameraView = new CameraView(this, mCamera);
            FrameLayout frameLayout = findViewById(R.id.camera_view);
            frameLayout.addView(mCameraView);
        }

        mCameraView.setListener(this);

    }

    @Override
    public void takephoto() {
        mCameraView.takePhoto();
    }
}
