package com.hosiluan.simplecamera;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by User on 11/9/2017.
 */

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImage(File file) {

        if (file == null){
            Log.d("Luan","file == null");
        }else {
            Log.d("Luan","file != null");

        }
        new LoadImage().execute(file,this);
    }


    class LoadImage extends AsyncTask<Object, Object, Bitmap> {

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            ((ImageView) values[0]).setImageBitmap((Bitmap) values[1]);
            ((ImageView) values[0]).setRotation(90);
        }

        @Override
        protected Bitmap doInBackground(Object... objects) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream((File) objects[0]));

                if (bitmap == null){
                    Log.d("Luan","bitmap == null");
                }else {
                    Log.d("Luan","bitmap != null");

                }
                bitmap = getScaledBitmap(bitmap, ((ImageView) objects[1]).getWidth(),
                        ((ImageView) objects[1]).getHeight());

//                Log.d("Luan",((ImageView) objects[1]).getWidth() + " / " +
//                        ((ImageView) objects[1]).getHeight());

                publishProgress(((ImageView) objects[1]), bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {


        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = reqWidth;
        int nHeight = reqHeight;

        float parentRatio = (float) reqHeight / reqWidth;

        nHeight = bHeight;
        nWidth = (int) (reqWidth * 0.6);

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);
    }



}
