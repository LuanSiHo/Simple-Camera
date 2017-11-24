package com.hosiluan.simplecamera;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;

import static com.hosiluan.simplecamera.ListPhoToActivity.FILE_NAME;

public class PhotoActivity extends BaseActivity implements GestureDetector.OnGestureListener {
    float initialX;
    float initialY;
    private CustomImageView mImageView;
    private RelativeLayout mLinearLayoutPhoto;
    private int mCurrentImagePos = 0;

    private boolean mIsZoomIn = false;
    ArrayList<File> mFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setView();
        mDetector = new GestureDetectorCompat(getApplicationContext(), new MyGestureListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String name = intent.getStringExtra(FILE_NAME);
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        mFileList = getListFiles(imageDir);
        for (int i = 0; i < mFileList.size(); i++) {
            if (mFileList.get(i).getName().equals(name)) {
                mImageView.loadImage(mFileList.get(i));
                mCurrentImagePos = i;
            }
        }
    }

    private void setView() {
        mImageView = findViewById(R.id.img_image);
        mLinearLayoutPhoto = findViewById(R.id.linear_layout_photo);
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

    private double getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private double getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//
////        layoutParams.gravity = Gravity.CENTER;
//        double h = getScreenHeight();
//        double w = getScreenWidth();
//
//        double mAppRatio = w > h ? w / h : h / w;
//
//        layoutParams.height = (int) getScreenHeight();
//        layoutParams.width = (int) (getScreenHeight() / mAppRatio);
//
//        mImageView.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d("Luan", v + " / " + v1);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d("Luan", v + " / " + v1);
        Log.d("Luan", "onFling: " + motionEvent.toString() + motionEvent1.toString());

        return true;
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            final Animation zoomAnimation;
            if (mIsZoomIn) {
                zoomAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
                mIsZoomIn = false;
            } else {
                zoomAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                mIsZoomIn = true;
            }
            mImageView.startAnimation(zoomAnimation);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
    }


    private GestureDetectorCompat mDetector;


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initialX = e.getX();
                initialY = e.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                float finalX = e.getX();
                float finalY = e.getY();


                if (initialX < finalX) {
                    Log.d("Luan", "Left to Right swipe performed");
                    Log.d("Luan", mCurrentImagePos + " current");
                    if (mCurrentImagePos > 0){
                        mImageView.loadImage(mFileList.get(mCurrentImagePos - 1));
                        mCurrentImagePos = mCurrentImagePos - 1;
                    }
                }

                if (initialX > finalX) {
                    Log.d("Luan", "Right to Left swipe performed");
                    Log.d("Luan", mCurrentImagePos + " current");

                    if (mCurrentImagePos < mFileList.size() - 1){
                        mImageView.loadImage(mFileList.get(mCurrentImagePos + 1));
                        mCurrentImagePos = mCurrentImagePos + 1;
                    }
                }

                if (initialY < finalY) {
//                    Log.d("Luan", "Up to Down swipe performed");
                }

                if (initialY > finalY) {
//                    Log.d("Luan", "Down to Up swipe performed");
                }

                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d("Luan", "Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.d("Luan", "Movement occurred outside bounds of current screen element");
                break;
        }
        return super.onTouchEvent(e);
//        return mDetector.onTouchEvent(e);
    }


}
