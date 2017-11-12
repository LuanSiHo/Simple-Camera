package com.hosiluan.simplecamera;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.FeatureInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by User on 11/9/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<File> mListPhoto;
    Context mContext;
    RecyclerViewAdapterListener recyclerViewAdapterListener;

    private Boolean isLongClicked = false;
    private ArrayList<File> mSelectedList = new ArrayList<>();
    private ArrayList<String> highlightPosition = new ArrayList<>();
    private Boolean unHighLight = true;


    public ArrayList<File> getmSelectedList() {
        return mSelectedList;
    }

    public void setmSelectedList(ArrayList<File> mSelectedList) {
        this.mSelectedList = mSelectedList;
    }

    public Boolean getUnHighLight() {
        return unHighLight;
    }

    public void setUnHighLight(Boolean unHighLight) {
        this.unHighLight = unHighLight;
    }

    public Boolean getLongClicked() {
        return isLongClicked;
    }

    public void setLongClicked(Boolean longClicked) {
        isLongClicked = longClicked;
    }

    public RecyclerViewAdapter(ArrayList<File> mListPhoto, Context mContext,
                               RecyclerViewAdapterListener recyclerViewAdapterListener) {
        this.mListPhoto = mListPhoto;
        this.mContext = mContext;
        this.recyclerViewAdapterListener = recyclerViewAdapterListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Log.d("Luan",mListPhoto.get(position).getName());

        ContextWrapper contextWrapper = new ContextWrapper(mContext);
        File dir = contextWrapper.getDir("tempImageDir",Context.MODE_PRIVATE);

        String path = dir.getAbsolutePath();
        Log.d("Luan",path +  " path holer");

        Bitmap bitmap = loadImageFromStorage(path,mListPhoto.get(position).getName());
        if (bitmap != null){
            Log.d("Luan","bitmap != null");
            holder.imageView.setImageBitmap(bitmap);
            holder.imageView.setRotation(90);
        }else {
            Log.d("Luan","bitmap == null");
            holder.imageView.loadImage(mListPhoto.get(position));
        }

        if (unHighLight){
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLongClicked){
                    if (checkHighlightPositon(position)){
                            view.setBackgroundColor(Color.WHITE);
                            mSelectedList.remove(mListPhoto.get(position));
                            highlightPosition.remove(String.valueOf(position));

                    }else {
                            view.setBackgroundColor(Color.BLUE);
                            mSelectedList.add(mListPhoto.get(position));
                            highlightPosition.add(String.valueOf(position));
                    }
                }else {
                    recyclerViewAdapterListener.onItemClick(position);

                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setLongClicked(true);
                view.setBackgroundColor(Color.BLUE);
                mSelectedList.add(mListPhoto.get(position));
                recyclerViewAdapterListener.onItemLongClick();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListPhoto.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CustomImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_image);
        }
    }

    public boolean checkHighlightPositon(int position) {
        for (int i = 0; i < highlightPosition.size(); i++) {
            if (Integer.parseInt(highlightPosition.get(i)) == position) {
                return true;
            }
        }
        return false;
    }
    public void removeAllSelectedItem() {
        for (int i = 0; i < mSelectedList.size(); i++) {
            mSelectedList.remove(i);

        }
        for (int i = 0; i < highlightPosition.size(); i++) {
            highlightPosition.remove(i);
        }

    }

    /**
     * load image from internal storage
     */
    private Bitmap loadImageFromStorage(String path, String imageName) {
        Bitmap bitmap = null;
        try {
            File f = new File(path, imageName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public interface RecyclerViewAdapterListener{
        void onItemClick(int position);
        void onItemLongClick();
    }
}

