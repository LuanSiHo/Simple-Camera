package com.hosiluan.simplecamera;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

public class ListPhoToActivity extends BaseActivity
        implements RecyclerViewAdapter.RecyclerViewAdapterListener,
        FragmentPhotoToolbarDefault.DefaultToolbarFragmentListener,
        FragmentPhotoDeleteToolbar.DeleteToolbarFragmentListener {

    ArrayList<File> mListPhoto;
    GridView mListPhotoGridView;
    CustomGridAdapter mCustomGridAdapter;

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;
    public static final String FILE_NAME = "index to photo activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pho_to);
        setView();
        setEvent();
        initToolbar();
    }

    private void setView() {
        mListPhoto = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerViewAdapter = new RecyclerViewAdapter(mListPhoto, getApplicationContext(), this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
        }else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        }
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");

        ArrayList<File> mPhotos = getListFiles(imageDir);
        for (int i = 0; i < mPhotos.size(); i++) {
            mListPhoto.add(mPhotos.get(i));
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("Luan","hello");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Luan", "ORIENTATION_LANDSCAPE");
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
            Log.d("Luan", "ORIENTATION_PORTRAIT");
        }
    }

    public void setFragment(Fragment fragment, String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_toolbar, fragment, TAG);
        fragmentTransaction.commit();
//        getActivity().getSupportFragmentManager().executePendingTransactions();
    }

    private void setEvent() {
    }


    private void initToolbar() {
        FragmentPhotoToolbarDefault userInfoToolbarFragment = FragmentPhotoToolbarDefault.create();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_toolbar, userInfoToolbarFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ListPhoToActivity.this, PhotoActivity.class);
        intent.putExtra(FILE_NAME, mListPhoto.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick() {
        FragmentPhotoDeleteToolbar fragmentPhotoDeleteToolbar = FragmentPhotoDeleteToolbar.create();
            setFragment(fragmentPhotoDeleteToolbar,"");
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public void onCancel() {


        mRecyclerViewAdapter.removeAllSelectedItem();
        mRecyclerViewAdapter.setUnHighLight(true);
        mRecyclerViewAdapter.setLongClicked(false);
        mRecyclerViewAdapter.notifyDataSetChanged();

        FragmentPhotoToolbarDefault fragmentPhotoToolbarDefault = FragmentPhotoToolbarDefault.create();
        setFragment(fragmentPhotoToolbarDefault,"");
    }

    @Override
    public void onDelete() {

        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListPhoToActivity.this);
        builder.setMessage("Bạn có muốn xóa những ảnh đã chọn")
                .setTitle("Delete")
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        ArrayList<File> files = mRecyclerViewAdapter.getmSelectedList();
                        Log.d("Luan",files.size() + " size");
                        for (int j = 0; j < files.size(); j ++){
                            files.get(j).delete();
                        }
                        FragmentPhotoToolbarDefault fragmentPhotoToolbarDefault = FragmentPhotoToolbarDefault.create();
                        setFragment(fragmentPhotoToolbarDefault,"");


                        for (int k = 0; k < files.size(); k ++){
                            mListPhoto.remove(files.get(k));
                        }
                        mRecyclerViewAdapter.notifyDataSetChanged();
                        mRecyclerViewAdapter.removeAllSelectedItem();
                        mRecyclerViewAdapter.setUnHighLight(true);
                        mRecyclerViewAdapter.setLongClicked(false);

                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }
}
