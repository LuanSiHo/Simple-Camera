package com.hosiluan.simplecamera.custom;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.hosiluan.simplecamera.general.Constant;
import com.hosiluan.simplecamera.ultils.CachingBitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by User on 11/9/2017.
 */

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {
    public  LoadImage mLoadImage;
    Bitmap bitmap = null;

    public CustomImageView(Context context) {
        super(context);
        // Set the gesture detector as the double tap
        // listener.
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImage(final File file) {

        new LoadImage().execute(file,this);
    }


    private void init(){

    }

    class LoadImage extends AsyncTask<Object, Object, Bitmap> {

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            ((ImageView) values[0]).setImageBitmap((Bitmap) values[1]);
        }

        @Override
        protected Bitmap doInBackground(Object... objects) {

            try {
                CachingBitmap cachingBitmap = CachingBitmap.getInstance();


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 2;

                bitmap = BitmapFactory.decodeStream(new FileInputStream((File) objects[0]), null, options);

//                bitmap = resize(bitmap, ((ImageView) objects[1]).getWidth(), ((ImageView) objects[1]).getHeight());
                cachingBitmap.addBitmapToMemoryCache(((File) objects[0]).getName(), bitmap);

                publishProgress(((ImageView) objects[1]), bitmap);


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


    private static Bitmap resize(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
            return bitmap;
        } else {
            return bitmap;
        }
    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {


        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = reqWidth;
        int nHeight = reqHeight;

        float parentRatio = (float) reqHeight / reqWidth;

        nHeight = bHeight;
        nWidth = (int) (reqWidth * 0.8);

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String imageName) throws IOException {
        ContextWrapper cw = new ContextWrapper(getContext());

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("tempImageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, imageName);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


}
