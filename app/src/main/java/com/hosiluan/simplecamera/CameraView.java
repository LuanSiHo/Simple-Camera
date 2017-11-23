package com.hosiluan.simplecamera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Policy;
import java.security.acl.LastOwnerException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;
import static android.hardware.Camera.*;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by Ho Si Luan on 11/7/2017.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public boolean isFlashOn;
    private boolean isTimerOn = false;

    public Bitmap mLastestThumnailBitmap;
    private boolean hasFlash;
    Camera.Parameters params;
    int currentZoomLevel = 1;

    private TakePhotoListener mTakePhotoListener;
    private Camera.Size mPreviewSize;
    private List<Camera.Size> mSupportedPreviewSizes;


    public boolean isTimerOn() {
        return isTimerOn;
    }

    public void setTimerOn(boolean timerOn) {
        isTimerOn = timerOn;
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }

    public void setFlashOn(boolean flashOn) {
        isFlashOn = flashOn;
    }

    public SurfaceHolder getmHolder() {
        return mHolder;
    }

    public void setmHolder(SurfaceHolder mHolder) {
        this.mHolder = mHolder;
    }

    public Camera getmCamera() {
        return mCamera;
    }

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }

    public CameraView(Context context, Camera camera) {
        super(context);

//        if (mCamera != null && mCamera != camera){
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = null;
//        }

        mCamera = camera;

        // supported preview sizes
//        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();

//        requestLayout();


        Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90);
        }

        if (display.getRotation() == Surface.ROTATION_90) {
            mCamera.setDisplayOrientation(0);
        }

        if (display.getRotation() == Surface.ROTATION_180) {
            mCamera.setDisplayOrientation(270);
        }

        if (display.getRotation() == Surface.ROTATION_270) {
            mCamera.setDisplayOrientation(180);
        }

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
//        final double ASPECT_TOLERANCE = 1;
//        double targetRatio = (double) h / w;
//
//        if (sizes == null)
//            return null;
//
//        Camera.Size optimalSize = null;
//        double minDiff = Double.MAX_VALUE;
//
//        int targetHeight = h;
//
//        for (Camera.Size size : sizes) {
//            double ratio = (double) size.height / size.width;
//            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
//                continue;
//
//            if (Math.abs(size.height - targetHeight) < minDiff) {
//                optimalSize = size;
//                minDiff = Math.abs(size.height - targetHeight);
//            }
//        }
//
//        if (optimalSize == null) {
//            minDiff = Double.MAX_VALUE;
//            for (Camera.Size size : sizes) {
//                if (Math.abs(size.height - targetHeight) < minDiff) {
//                    optimalSize = size;
//                    minDiff = Math.abs(size.height - targetHeight);
//                }
//            }
//        }
//        return optimalSize;
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//
//        if (mSupportedPreviewSizes != null) {
//            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
//        }
//
//        if (mPreviewSize!=null) {
//            float ratio;
//            if(mPreviewSize.height >= mPreviewSize.width)
//                ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
//            else
//                ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;
//
//            // One of these methods should be used, second method squishes preview slightly
//            setMeasuredDimension(width, (int) (width * ratio));
//            //        setMeasuredDimension((int) (width * ratio), height);
//        }
//    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        Log.d("Luan", "surface create");
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
////            mCamera.setPreviewDisplay(mHolder);
////            mCamera.startPreview();
//
////            mCamera.stopPreview();
//
//            if (mCamera != null){
////                Camera.Parameters parameters =  mCamera.getParameters();
////                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
////                mCamera.setParameters(parameters);
//                mCamera.setPreviewDisplay(mHolder);
//                mCamera.startPreview();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("Luan", "surface change");

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("Luan", "surface destroy");

//        mCamera.stopPreview();
//        mCamera.release();
    }

    public void switchCamera(int currentCameraId) {
        mCamera.stopPreview();
        mCamera.release();

        //swap the id of the camera to be used
        if (currentCameraId == CameraInfo.CAMERA_FACING_BACK) {
            currentCameraId = CameraInfo.CAMERA_FACING_FRONT;
        } else {
            currentCameraId = CameraInfo.CAMERA_FACING_BACK;
        }
        mCamera = open(currentCameraId);

        Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation() == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90);
        }

        if (display.getRotation() == Surface.ROTATION_90) {
            mCamera.setDisplayOrientation(0);
        }

        if (display.getRotation() == Surface.ROTATION_180) {
            mCamera.setDisplayOrientation(270);
        }

        if (display.getRotation() == Surface.ROTATION_270) {
            mCamera.setDisplayOrientation(180);
        }

        try {
//            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void changeFlashStatus() {

        if (isFlashOn) {
            turnOffFlash();
        } else {
            turnOnFlash();
        }
    }

    public void takePhoto() {

        params = mCamera.getParameters();

        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
        for (int i = 0; i < sizes.size(); i++) {
            if (sizes.get(i).width > size.width) size = sizes.get(i);
        }

        params.setPictureSize(size.width, size.height);

        mCamera.setParameters(params);
        if (isFlashOn) {
            turnOnFlash();
        }
        mCamera.takePicture(myShutterCallback, myPictureCallback_RAW, mPicture);
        turnOffFlash();

    }


    // Get the camera
    private void getCamera() {
        if (mCamera == null) {
            try {
                mCamera = open();
                params = mCamera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Luan", e.getMessage());
            }
        }
    }

    // Turning On flash
    private void turnOnFlash() {
        if (mCamera == null) {
            return;
        }

        params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(params);
    }

    // Turning Off flash
    private void turnOffFlash() {

        if (mCamera == null) {
            return;
        }
        params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
    }


    ShutterCallback myShutterCallback = new ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };

    PictureCallback myPictureCallback_RAW = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }
    };

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("Luan", "Error creating media file, check storage permissions: ");
                return;
            }
            String path = pictureFile.getAbsolutePath();

            try {

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                Matrix matrix = new Matrix();

                switch (display.getRotation()) {
                    case Surface.ROTATION_0:
                        if (MainActivity.sSavedCameraId == 1) {
                            matrix.postRotate(270);
                        } else {
                            matrix.postRotate(90);
                        }
                        break;
                    case Surface.ROTATION_90:
                        matrix.postRotate(0);
                        break;
                    case Surface.ROTATION_180:
                        matrix.postRotate(270);
                        break;
                    case Surface.ROTATION_270:
                        matrix.postRotate(180);
                        break;
                }
                bitmap = resize(bitmap, bitmap.getWidth(), bitmap.getHeight());

                if (MainActivity.sSavedCameraId == 1) {
                    matrix.postScale(-1, 1);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                bitmap = getScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight());

                FileOutputStream fos = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);


                mTakePhotoListener.setBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Log.d("Luan", "File not found: " + e.getMessage());
            }

            refreshCamera();
        }
    };

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

    public void refreshCamera() {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }


    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void zoomIn() {
        params = mCamera.getParameters();
        if (params.isZoomSupported()) {
            int maxZoomLevel = params.getMaxZoom();
            Log.d("Luan", "max zoom level " + maxZoomLevel);
            if (currentZoomLevel < maxZoomLevel - 5) {
                currentZoomLevel += 5;
                params.setZoom(currentZoomLevel);
//                params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
                mCamera.setParameters(params);
            }
        }
    }

    public void zoomOut() {
        params = mCamera.getParameters();
        if (params.isZoomSupported()) {
            if (currentZoomLevel >= 5) {
                currentZoomLevel -= 5;
                params.setZoom(currentZoomLevel);
                mCamera.setParameters(params);
            }
        }
    }

    public void changeBrightness(int bright) {
        Log.d("Luan", bright + " brigh");

        params = mCamera.getParameters();
        params.setExposureCompensation(bright);
        mCamera.setParameters(params);
        mCamera.startPreview();
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

    public interface TakePhotoListener {
        void setBitmap(Bitmap bitmap);
    }

    public void setListener(TakePhotoListener mTakePhotoListener) {
        this.mTakePhotoListener = mTakePhotoListener;
    }


}
