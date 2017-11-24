package com.hosiluan.simplecamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hosiluan.simplecamera.general.Common;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseActivity implements CameraView.TakePhotoListener,
        BaseActivity.PermissionAcceptedListener, BaseActivity.TakePictureListener{

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    int mCountDownTimerIndex = 4;

    public ImageView mLastestThumnailImageView;
    private Button mSwitchCameraButton;
    private ImageButton mSwitchCameraImageButton, mChangeFlashImageButton,
            mTakePhotoImageButton, mZoomInImageButton, mZoomOutImageButton, mTimerImageButton;
    private TextView mTimerStatusTextView, mTimerTextView;
    private int mCurrentCameraId = 0;
    public static int sSavedCameraId = -1;
    private SeekBar mSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        setView();
        setEvent();
    }

    private void ShowExif(ExifInterface exif) {
        String myAttribute = "Exif information ---\n";
        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif);
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif);
        Log.d("Luan", myAttribute);
    }

    private String getTagString(String tag, ExifInterface exif) {
        return (tag + " : " + exif.getAttribute(tag) + "\n");
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPermissionListener(this);
        setTakePictureListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (sSavedCameraId >= 0) {
                mCamera = Camera.open(sSavedCameraId);
            } else {
                mCamera = Camera.open();
            }

            if (mCamera != null) {
                mCameraView = new CameraView(this, mCamera);
                FrameLayout frameLayout = findViewById(R.id.camera_view);
                frameLayout.addView(mCameraView);
                mCameraView.setListener(this);
                mCameraView.refreshCamera();

            }
        }
// else {
//            if (sSavedCameraId >= 0) {
//                mCamera = Camera.open(sSavedCameraId);
//            } else {
//                mCamera = Camera.open();
//            }
//            if (mCamera != null) {
//                mCameraView = new CameraView(this, mCamera);
//                FrameLayout frameLayout = findViewById(R.id.camera_view);
//                frameLayout.addView(mCameraView);
//                mCameraView.setListener(this);
//                mCameraView.refreshCamera();
//            }
//        }
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
                startActivity(new Intent(MainActivity.this, ListPhoToActivity.class));
            }
        });

        mSwitchCameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCameraView.switchCamera(mCurrentCameraId);
                if (mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCurrentCameraId = 1;
                    sSavedCameraId = 1;
                } else {
                    mCurrentCameraId = 0;
                    sSavedCameraId = 0;
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
//            bitmap = getScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight());
            mLastestThumnailImageView.setImageBitmap(bitmap);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (sSavedCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            outState.putInt(Common.CURRENT_CAMERA_ID, 0);
        } else if (sSavedCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            outState.putInt(Common.CURRENT_CAMERA_ID, 1);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sSavedCameraId = savedInstanceState.getInt(Common.CURRENT_CAMERA_ID);
    }

}
