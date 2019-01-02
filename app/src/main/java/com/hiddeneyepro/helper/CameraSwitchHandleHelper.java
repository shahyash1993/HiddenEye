package com.hiddeneyepro.helper;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.design.widget.FloatingActionButton;

import com.hiddeneyepro.R;


public class CameraSwitchHandleHelper {

    public static void toggleSwitchCameraImage(Boolean isSwitchCameraOn, FloatingActionButton btnSwitchCamera){
        if(isSwitchCameraOn) {
            btnSwitchCamera.setImageResource(R.drawable.switch_camera_off);
            btnSwitchCamera.setRippleColor(Color.WHITE);
            btnSwitchCamera.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        }
         else{
            btnSwitchCamera.setImageResource(R.drawable.switch_camera_on);
                btnSwitchCamera.setRippleColor(Color.BLACK);
                btnSwitchCamera.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }
    }//end toggleSwitchCameraImage

    public static int findFrontFacingCamera(Boolean cameraFront) {

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;

    }

    public static int findBackFacingCamera(Boolean cameraFront) {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;

            }
        }
        return cameraId;
    }

    /*public static Camera chooseCamera(Boolean cameraFront, Camera mCamera, SurfaceHolder previewHolder, Camera.PictureCallback mPicture) {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera(cameraFront);
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                Log.e(">>","Helper: findBack, cam obj:"+mCamera);
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(0);
                //MAY BE IMP
                //mPicture = getPictureCallback();
                mCamera = refreshCamera(mCamera, previewHolder);
            }
        } else {
            int cameraId = findFrontFacingCamera(cameraFront);
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                Log.e(">>","Helper: findFrontFacingCam, cam obj:"+mCamera);
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(0);
                //MAY BE IMP
                //mPicture = getPictureCallback();
                mCamera = refreshCamera(mCamera, previewHolder);
            }
        }
        return mCamera;
    }//end method

    public static Camera refreshCamera(Camera mCamera, SurfaceHolder mHolder) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return mCamera;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        //setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(">>", "Error starting camera preview: " + e.getMessage());
        }
        return mCamera;
    }*/





}//end class
