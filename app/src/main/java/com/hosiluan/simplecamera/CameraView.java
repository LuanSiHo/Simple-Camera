package com.hosiluan.simplecamera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Ho Si Luan on 11/7/2017.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;

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
        getmCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
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
        mCamera.release();
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

        mCamera.setDisplayOrientation(90);

        try {
            mCamera.setPreviewDisplay(mHolder);

        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.startPreview();

    }

    public void changeFlashStatus() {

        if (isFlashOn) {
            turnOffFlash();
        } else {
            turnOnFlash();
        }
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
        if (!isFlashOn) {
            if (mCamera == null) {
                return;
            }

            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
            isFlashOn = true;

        }

    }

    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (mCamera == null) {
                return;
            }

            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            isFlashOn = false;
        }
    }


}
