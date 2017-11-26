package com.hosiluan.simplecamera.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hosiluan.simplecamera.R;
import com.hosiluan.simplecamera.custom.CustomImageView;
import com.hosiluan.simplecamera.general.Common;
import com.hosiluan.simplecamera.ultils.CachingBitmap;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Ho Si Luan on 11/24/2017.
 */

public class RecyclerViewSinglePhotoAdapter extends RecyclerView.Adapter<RecyclerViewSinglePhotoAdapter.MyHolder> {

    ArrayList<File> mPhotoList;
    Context mContext;

    public RecyclerViewSinglePhotoAdapter(ArrayList<File> mPhotoList, Context mContext) {
        this.mPhotoList = mPhotoList;
        this.mContext = mContext;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_full_size,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CachingBitmap cachingBitmap = CachingBitmap.getInstance();
        Bitmap bitmap = cachingBitmap.getBitmapFromMemCache(mPhotoList.get(position).getName());
        if (bitmap != null){
            holder.imageView.setImageBitmap(bitmap);
        }else {
            holder.imageView.loadImage(mPhotoList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mPhotoList != null)
        {
            return mPhotoList.size();
        }
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CustomImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_image);
        }
    }
}
