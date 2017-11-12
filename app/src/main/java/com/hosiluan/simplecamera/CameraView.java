package com.hosiluan.simplecamera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.security.acl.LastOwnerException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        mCamera = camera;
        mCamera.setDisplayOrientation(90);

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try {
            if (mCamera != null){
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        if (mHolder.getSurface() == null) {
            return;
        }

        mCamera.stopPreview();

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        mCamera.stopPreview();
//        mCamera.release();
    }

    public void switchCamera(int currentCameraId) {
        mCamera.stopPreview();
        mCamera.release();

        //swap the id of the camera to be used
        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        mCamera = Camera.open(currentCameraId);


        try {
            mCamera.setDisplayOrientation(90);
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
            if (sizes.get(i).width > size.width)
                size = sizes.get(i);
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
                mCamera = Camera.open();
                params = mCamera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Luan", e.getMessage());
            }
        }
    }

    // Turning On flash
    private void turnOnFlash() {
        if (mCamera == null){
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


    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };

    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }
    };

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("Luan", "Error creating media file, check storage permissions: ");
                return;
            }
            String path = pictureFile.getAbsolutePath();

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();


                File file = new File(path);
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(pictureFile));

                mTakePhotoListener.setBitmap(bitmap);


            } catch (FileNotFoundException e) {
                Log.d("Luan", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Luan", "Error accessing file: " + e.getMessage());
            }

            refreshCamera();

        }
    };

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

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
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
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void zoomIn(){
        params= mCamera.getParameters();
        if (params.isZoomSupported()){
            int maxZoomLevel = params.getMaxZoom();
            Log.d("Luan","max zoom level " + maxZoomLevel);
            if (currentZoomLevel < maxZoomLevel-5){
                currentZoomLevel+= 5;
                params.setZoom(currentZoomLevel);
//                params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
                mCamera.setParameters(params);
            }
        }
    }

    public void zoomOut(){
        params= mCamera.getParameters();
        if (params.isZoomSupported()) {
            if (currentZoomLevel >= 5){
                currentZoomLevel -= 5;
                params.setZoom(currentZoomLevel);
                mCamera.setParameters(params);
            }
        }
    }

    public void changeBrightness(int bright){
        Log.d("Luan",bright + " brigh");

        params = mCamera.getParameters();
        params.setExposureCompensation(bright);
        mCamera.setParameters(params);
        mCamera.startPreview();
    }

    public interface TakePhotoListener{
        void setBitmap(Bitmap bitmap);
    }

    public void setListener(TakePhotoListener mTakePhotoListener){
        this.mTakePhotoListener = mTakePhotoListener;
    }
}
