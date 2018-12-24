package com.hiddeneyepro.motiondetection;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MotionDetector {
    class MotionDetectorThread extends Thread {
        private AtomicBoolean isRunning = new AtomicBoolean(true);

        public void stopDetection() {
            Log.e(">>","Thread | stopDetection()");
            isRunning.set(false);
            //worker.stopDetection();
        }
        public void startDetection() {
            Log.e(">>","Thread | startDetection()");
            isRunning.set(true);
            //worker.startDetection();
        }
        public AtomicBoolean isRunning() {
            return isRunning;
        }

        @Override
        public void run() {
            while (isRunning.get()) {
                long now = System.currentTimeMillis();
                if (now - lastCheck > checkInterval) {
                    lastCheck = now;

                    if (nextData.get() != null) {
                        int[] img = ImageProcessing.decodeYUV420SPtoLuma(nextData.get(), nextWidth.get(), nextHeight.get());

                        // check if it is too dark
                        int lumaSum = 0;
                        for (int i : img) {
                            lumaSum += i;
                        }
                        if (lumaSum < minLuma) {
                            if (motionDetectorCallback != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        motionDetectorCallback.onTooDark();
                                    }
                                });
                            }
                        } else if (detector.detect(img, nextWidth.get(), nextHeight.get())) {
                            // check
                            if (motionDetectorCallback != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        motionDetectorCallback.onMotionDetected();
                                    }
                                });
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private final AggregateLumaMotionDetection detector;
    private long checkInterval = 500;
    private long lastCheck = 0;
    private MotionDetectorCallback motionDetectorCallback;
    private Handler mHandler = new Handler();

    private AtomicReference<byte[]> nextData = new AtomicReference<>();
    private AtomicInteger nextWidth = new AtomicInteger();
    private AtomicInteger nextHeight = new AtomicInteger();
    private int minLuma = 1000;
    private MotionDetectorThread worker;

    /*private Camera mCamera;
    private boolean inPreview;
    private SurfaceHolder previewHolder;
    private Context mContext;
    private SurfaceView mSurface;*/

//    public MotionDetector(Context context, SurfaceView previewSurface, Camera cam) {
    public MotionDetector() {
        detector = new AggregateLumaMotionDetection();
        //mContext = context;
        //mSurface = previewSurface;
        //mCamera = cam;
    }


    public void setMotionDetectorCallback(MotionDetectorCallback motionDetectorCallback) {
        this.motionDetectorCallback = motionDetectorCallback;
    }

    public void consume(byte[] data, int width, int height) {
        //Log.e(">>Consume: ",data.toString());

        nextData.set(data);
        nextWidth.set(width);
        nextHeight.set(height);
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public void setMinLuma(int minLuma) {
        this.minLuma = minLuma;
    }

    public void setLeniency(int l) {
        Log.e(">>","MotionDetector: Lenience: "+l);
        detector.setLeniency(l);
    }

    public MotionDetectorThread getWorker(){
        return worker;
    }

    public void startDetection(){
        //this.setMotionDetectorCallback(null);
        if(worker!=null)
            worker.startDetection();
        else
            Log.e(">>","MotionDetector | startDetection() | worker is null");
    }

    public void stopDetection(){
        if(worker!=null)
            worker.stopDetection();
        else
            Log.e(">>", "MotionDetector | stopDetection() | worker is null");
    }

    public void onPause() {
        //releaseCamera();
        //if (previewHolder != null) previewHolder.removeCallback(surfaceCallback);
        if (worker != null)
            worker.stopDetection();
        else
            Log.e(">>", "MotionDetector | onPause() | worker is null");

    }

    public void onResume() {
        worker = new MotionDetectorThread();
        worker.start();

        /*if (checkCameraHardware()) {
            mCamera = getCameraInstance();

            worker = new MotionDetectorThread();
            worker.start();

            // configure preview
            previewHolder = mSurface.getHolder();
            previewHolder.addCallback(surfaceCallback);
            previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }*/
    }

    //All Static Methods
    public static boolean checkCameraHardware(Context mContext) {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    public static Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) result = size;
                }
            }
        }

        return result;
    }

    //ALL unused Methods:
    /*private Camera getCameraInstance() {
        Camera c = null;

        try {
            if (Camera.getNumberOfCameras() >= 2) {
                //if you want to open front facing camera use this line
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                c = Camera.open();
            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            //txtStatus.setText("Kamera nicht zur Benutzung freigegeben");
        }
        return c; // returns null if camera is unavailable
    }*/

    /*private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

     *//**
     * {@inheritDoc}
     *//*
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) return;
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) return;

            consume(data, size.width, size.height);
        }
    };*/


    /* private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

     *//**
     * {@inheritDoc}
     *//*
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(previewHolder);
                mCamera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("MotionDetector", "Exception in setPreviewDisplay()", t);
            }
        }

        *//**
     * {@inheritDoc}
     *//*
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size size = getBestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d("MotionDetector", "Using width=" + size.width + " height=" + size.height);
            }
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            inPreview = true;
        }

        *//**
     * {@inheritDoc}
     *//*
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };*/

    /*private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            if (inPreview) mCamera.stopPreview();
            inPreview = false;
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }*/
}//end class
