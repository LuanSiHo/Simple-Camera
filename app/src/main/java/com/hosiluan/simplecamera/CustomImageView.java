package com.hosiluan.simplecamera;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.WINDOW_SERVICE;

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
        new LoadImage().execute(file, this);
    }



    class LoadImage extends AsyncTask<Object, Object, Bitmap> {

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            ((ImageView) values[0]).setImageBitmap((Bitmap) values[1]);
        }

        @Override
        protected Bitmap doInBackground(Object... objects) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream((File) objects[0]));

//                bitmap = getScaledBitmap(bitmap, ((ImageView) objects[1]).getWidth(),
//                        ((ImageView) objects[1]).getHeight());

                bitmap = resize(bitmap, ((ImageView) objects[1]).getWidth(), ((ImageView) objects[1]).getHeight());

                CachingBitmap cachingBitmap = CachingBitmap.getInstance();
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


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
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
