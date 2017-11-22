package com.hosiluan.simplecamera;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import static com.hosiluan.simplecamera.ListPhoToActivity.FILE_NAME;

public class PhotoActivity extends BaseActivity {


    private CustomImageView mImageView;
    private LinearLayout mLinearLayoutPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String name = intent.getStringExtra(FILE_NAME);
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        ArrayList<File> files = getListFiles(imageDir);
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getName().equals(name)) {
                mImageView.loadImage(files.get(i));
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

//        if (newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE){
//
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                    FrameLayout.LayoutParams.MATCH_PARENT,
//                    FrameLayout.LayoutParams.MATCH_PARENT);
//            layoutParams.gravity = Gravity.CENTER;
//            layoutParams.height = (int) getScreenHeight();
//            double h = getScreenHeight();
//            double w = getScreenWidth();
//
//            double mAppRatio = w > h ? w / h : h / w;
//
//            layoutParams.width = (int) (getScreenHeight() / mAppRatio);
//            mLinearLayoutPhoto.setLayoutParams(layoutParams);
//
//
//        }else if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT){
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                    FrameLayout.LayoutParams.MATCH_PARENT,
//                    FrameLayout.LayoutParams.MATCH_PARENT);
//            layoutParams.gravity = Gravity.CENTER;
//            layoutParams.width = (int) getScreenWidth();
//            double h = getScreenHeight();
//            double w = getScreenWidth();
//            double mAppRatio = w > h ? w / h : h / w;
//
//            layoutParams.height = (int) (getScreenWidth() / mAppRatio);
//            mLinearLayoutPhoto.setLayoutParams(layoutParams);
//        }


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        layoutParams.gravity = Gravity.CENTER;
        double h = getScreenHeight();
        double w = getScreenWidth();

        double mAppRatio = w > h ? w / h : h / w;

        layoutParams.height = (int) getScreenHeight();
        layoutParams.width = (int) (getScreenHeight() / mAppRatio);

        mLinearLayoutPhoto.setLayoutParams(layoutParams);

    }
}
