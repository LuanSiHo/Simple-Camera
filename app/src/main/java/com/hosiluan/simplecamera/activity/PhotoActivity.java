package com.hosiluan.simplecamera.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.hosiluan.simplecamera.adapter.RecyclerViewSinglePhotoAdapter;
import com.hosiluan.simplecamera.custom.CustomImageView;
import com.hosiluan.simplecamera.R;
import com.hosiluan.simplecamera.general.Common;
import com.hosiluan.simplecamera.ultils.CachingBitmap;

import java.io.File;
import java.util.ArrayList;

import static com.hosiluan.simplecamera.general.Constant.FILE_NAME;

public class PhotoActivity extends BaseActivity {

    int index = 0;

    private CustomImageView mImageView;
    private int mCurrentImagePos = 0;

    private boolean mIsZoomIn = false;
    ArrayList<File> mFileList;

    private RecyclerView mRecyclerViewPhoto;
    private RecyclerViewSinglePhotoAdapter mRecyclerViewSinglePhotoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setView();
        setEvent();

    }

    private void setEvent() {
        mRecyclerViewPhoto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == recyclerView.SCROLL_STATE_IDLE) {
                    mRecyclerViewPhoto.scrollToPosition(mCurrentImagePos + index);
                    if (mCurrentImagePos > 0) {
                        mCurrentImagePos = mCurrentImagePos + index;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int x = 0;

                int currentOffset = mCurrentImagePos * mRecyclerViewPhoto.getMeasuredWidth();

                int scrolledOffset = mRecyclerViewPhoto.computeHorizontalScrollOffset() - currentOffset;

                if (scrolledOffset > 0) {

                    if (currentOffset > 0) {
                        index = scrolledOffset / currentOffset;
                        int temp = scrolledOffset % currentOffset;
                        if (temp > mRecyclerViewPhoto.getMeasuredWidth() / 2) {
                            index++;
                        }
                    } else {
                        index = scrolledOffset / mRecyclerViewPhoto.getMeasuredWidth();
                        int temp = scrolledOffset % mRecyclerViewPhoto.getMeasuredWidth();
                        if (temp > mRecyclerViewPhoto.getMeasuredWidth() / 2) {
                            index++;
                        }
                    }

                } else {
                    if (currentOffset > 0) {
                        index = scrolledOffset / currentOffset;
                        int temp = scrolledOffset % currentOffset;
                        if (-temp > mRecyclerViewPhoto.getMeasuredWidth() / 2) {
                            index--;
                        }
                    } else {
                        index = scrolledOffset / mRecyclerViewPhoto.getMeasuredWidth();
                        int temp = scrolledOffset % mRecyclerViewPhoto.getMeasuredWidth();
                        if (temp > mRecyclerViewPhoto.getMeasuredWidth() / 2) {
                            index++;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImageList();
    }

    private void setView() {
        mImageView = findViewById(R.id.img_image);

        mFileList = new ArrayList<>();
        mRecyclerViewPhoto = findViewById(R.id.recyclerview_photo);
        mRecyclerViewSinglePhotoAdapter = new RecyclerViewSinglePhotoAdapter(mFileList, getApplicationContext());

        mRecyclerViewPhoto.setAdapter(mRecyclerViewSinglePhotoAdapter);
        mRecyclerViewPhoto.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void getImageList() {
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

        ArrayList<File> files = getListFiles(imageDir);
        for (int i = 0; i < files.size(); i++) {
            mFileList.add(files.get(i));
        }
        mRecyclerViewSinglePhotoAdapter.notifyDataSetChanged();
        Intent intent = getIntent();
        String name = intent.getStringExtra(FILE_NAME);
        for (int i = 0; i < mFileList.size(); i++) {
            if (mFileList.get(i).getName().equals(name)) {
                mRecyclerViewPhoto.scrollToPosition(i);
                mCurrentImagePos = i;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



}


