package com.hosiluan.simplecamera.general;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Ho Si Luan on 11/24/2017.
 */

public class Common {
    /**
     * load image from internal storage
     */
    public static Bitmap loadImageFromStorage(String path, String imageName) {
        Bitmap bitmap = null;
        try {
            File f = new File(path, imageName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
