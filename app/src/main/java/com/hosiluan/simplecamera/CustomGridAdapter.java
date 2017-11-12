package com.hosiluan.simplecamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by User on 11/9/2017.
 */

public class CustomGridAdapter extends BaseAdapter {

    ArrayList<File> mListPhoto;
    Context mContext;


    public CustomGridAdapter(ArrayList<File> mListPhoto, Context mContext) {
        this.mListPhoto = mListPhoto;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return mListPhoto.size();
    }

    @Override
    public Object getItem(int i) {
        return mListPhoto.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        Bitmap bitmap = null;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_image, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.img_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Log.d("Luan",i + " called");
        new LoadImage().execute(mListPhoto.get(i),viewHolder.imageView);

        return view;
    }

    class ViewHolder {
        ImageView imageView;
    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {


        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = reqWidth;
        int nHeight = reqHeight;

        float parentRatio = (float) reqHeight / reqWidth;

        nHeight = bHeight;
        nWidth = (int) (reqWidth * 0.7);

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);
    }

    class LoadImage extends AsyncTask<Object, Object, Bitmap> {

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            ((ImageView)values[0]).setImageBitmap((Bitmap) values[1]);
        }

        @Override
        protected Bitmap doInBackground(Object... objects) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream((File) objects[0]));
//                Log.d("Luan",((ImageView)objects[1]).getWidth() + " / " + ((ImageView)objects[1]).getHeight());

                bitmap = getScaledBitmap(bitmap, ((ImageView)objects[1]).getWidth(), ((ImageView)objects[1]).getHeight());


                publishProgress(((ImageView)objects[1]),bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return bitmap;
        }



        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }

}
