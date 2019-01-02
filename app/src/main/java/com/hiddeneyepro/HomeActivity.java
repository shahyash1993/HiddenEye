package com.hiddeneyepro;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.hiddeneyepro.helper.ActivityHelper;
import com.hiddeneyepro.helper.CameraSwitchHandleHelper;
import com.hiddeneyepro.helper.Config;
import com.hiddeneyepro.helper.CountDownHelper;
import com.hiddeneyepro.helper.FlashHandleHelper;
import com.hiddeneyepro.helper.MyAdminLocker;
import com.hiddeneyepro.motiondetection.MotionDetector;
import com.hiddeneyepro.motiondetection.MotionDetectorCallback;
import com.hiddeneyepro.helper.ActivityHelper;
import com.hiddeneyepro.helper.Config;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

    public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

        //
        //  Variable Declaration
        //

        //For Lock
        int detectedCounter = 0;
        private static final int RESULT_ENABLE = 13;

        private DevicePolicyManager devicePolicyManager;
        private ActivityManager activityManager;
        private ComponentName componentName;

        //Camera
        private Camera mCamera;
        private Camera.PictureCallback mPicture;
        Camera.Parameters params;
        boolean isSafeToTakePicture = true;

        public FloatingActionButton btnSwitchCamera, btnFlash, btnMotionDetector, btnCall911, btnBrightness;
        private Context myContext;

        private boolean cameraFront = false;
        public SurfaceView camSurfaceView;
        private SurfaceHolder previewHolder;

        //CountDown
        private TextView countDownTV;
        private boolean inPreview;

        //Brighness,  Flash
        boolean isFlashOn, isMotionDetectionOn, isBrightnessHigh, isAlarmOff = true;
        boolean hasFlash;

        private MotionDetector motionDetector;

        //Sound items
        private SoundPool soundPool;
        int soundID;

        //userDetails
        String username, userEmail, userPhoneNumber;
        SharedPreferences pref;
        SharedPreferences.Editor editor;

        //WakeLock
        PowerManager manager;
        PowerManager.WakeLock wl;

        //New Brightness
        int brightness;
        ContentResolver cResolver;
        Window window;

        LinearLayout sensitivityLL, counterLL;
        private final String TAG = Config.TAG + getClass().getSimpleName() + ">>";

        //Timer
        private CountDownTimer countDownTimer;
        private long timeLeftInMilliSeconds;

        Spinner counterSpinner, sensitivitySpinner;

        ObjectAnimator animator;

        private DrawerLayout mDrawerLayout;
        private ActionBarDrawerToggle mActionBarDrawerToggle;
        private NavigationView mNavigationView;

        //////////////////
        //////////////////
        //  onCreate    //
        //////////////////
        //////////////////

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.e(TAG, "-------------------------------HiddenEye STARTED-------------------------------");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            forLock();

            spinnerInit();
            init();
            newInit();


        }//end of onCreate

        private void forLock() {
            devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            componentName = new ComponentName(this, MyAdminLocker.class);

            boolean isActive = devicePolicyManager.isAdminActive(componentName);

            if(!isActive){
                Toast.makeText(this, "Enable Admin Permissions Please.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "HiddenEye Requires Admin Permission so that whenever " +
                        "Suspicious Activity is detected, HiddenEye can lock the phone and disable intruder from terminating the siren. " +
                        "Please Click on \"Activate\".");

                startActivityForResult(intent,RESULT_ENABLE);
            }

        }//end forLock

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode){
                case RESULT_ENABLE:
                    if(resultCode == Activity.RESULT_OK)
                        Toast.makeText(myContext, "You Enabled Admin Permissions", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(myContext, "You did not Enabled Admin Permissions", Toast.LENGTH_SHORT).show();

            }//end switch
        }//end onActRes

        private void spinnerInit() {
            sensitivityLL = (LinearLayout) findViewById(R.id.sensitivityLL);

            sensitivitySpinner = (Spinner) findViewById(R.id.sensitivitySpinner);
            ArrayAdapter<CharSequence> sensitivityAdapter = ArrayAdapter.createFromResource(this,
                    R.array.sensitivity_array, android.R.layout.simple_spinner_item);
            sensitivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sensitivitySpinner.setAdapter(sensitivityAdapter);
            sensitivitySpinner.setOnItemSelectedListener(this);

            counterLL = (LinearLayout) findViewById(R.id.counterLL);
            counterSpinner = (Spinner) findViewById(R.id.counterSpinner);
            ArrayAdapter<CharSequence> counterAdapter = ArrayAdapter.createFromResource(this,
                    R.array.counter_array, android.R.layout.simple_spinner_item);
            counterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            counterSpinner.setAdapter(counterAdapter);
            counterSpinner.setOnItemSelectedListener(this);
        }


        ////////My Work Area

        private void newInit() {
            //timer
            timeLeftInMilliSeconds = Config.TIMER * 1000;

            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            soundID = soundPool.load(this, R.raw.siren, 1);

            myContext = this;
            countDownTV = (TextView) findViewById(R.id.countDownTV);
            camSurfaceView = (SurfaceView) findViewById(R.id.camSurfaceView);


            //@suspicious
            pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, 0); // 0 - for private mode
            userPhoneNumber = pref.getString("phone_number", Config.ADMIN_PHONE_NUMBER);
            userEmail = pref.getString("email", Config.ADMIN_EMAIL);
            username = pref.getString("username", Config.ADMIN_USERNAME);

            //init Brighness
            initBrightness();

            //Wake Lock config & Brightness
            manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Config.WAKE_LOCK_NAME);
            wl.acquire();

            //Initialize Motion Detector
            motionDetector = new MotionDetector();

/*        Log.e(TAG,"newInit | Setting Leniency of md: "+Config.LENIENCY);
        motionDetector.setLeniency(Config.LENIENCY);
        motionDetector.setCheckInterval(Config.CHECK_INTERVAL);
        motionDetector.stopDetection();*/
        }//end newInit

        //
        //  initBrighness: performs basics of Brightness
        //
        private void initBrightness() {
            isBrightnessHigh = true;
            cResolver = getContentResolver();
            window = getWindow();

            try {
                brightness = android.provider.Settings.System.getInt(cResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS);
                Log.e(TAG, "Brightness before: " + brightness);
            } catch (Settings.SettingNotFoundException e) {
                Log.e("Error", "Cannot access system brightness");
                e.printStackTrace();
            }

            //android.provider.Settings.System.putInt(cResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
            WindowManager.LayoutParams layoutpars = window.getAttributes();

            //brightness=25000;
            layoutpars.screenBrightness = 1f;
            Log.e(TAG, "Brightness after: " + brightness);

            window.setAttributes(layoutpars);
        }//end initBrightness


        //
        //  handleBrighnessClick()
        //
        private void handleBrightnessClick() {
            btnBrightness = (FloatingActionButton) findViewById(R.id.btnBrightness);
            btnBrightness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WindowManager.LayoutParams lo_params = getWindow().getAttributes();
                    lo_params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

                    lo_params = ActivityHelper.toggleBrightness(isBrightnessHigh, lo_params, btnBrightness);

                    isBrightnessHigh = !isBrightnessHigh;
                    getWindow().setAttributes(lo_params);
                }//end onClick

            });
        }

        //
        //  Flash
        //
        private void handleFlash() {
            //Flash Activity
            btnFlash = (FloatingActionButton) findViewById(R.id.btnFlash);
            try {
                if (hasFlash()) {
                    btnFlash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isFlashOn) {
                                Toast.makeText(myContext, "Turning off Flash. Please Wait!", Toast.LENGTH_SHORT).show();
                                new FlashHandleHelper().turnOffFlash(myContext, mCamera, isFlashOn, params, btnFlash);
                            } else {
                                Toast.makeText(myContext, "Turning on Flash. Please Wait!", Toast.LENGTH_SHORT).show();
                                new FlashHandleHelper().turnOnFlash(myContext, mCamera, isFlashOn, params, btnFlash);
                            }
                            isFlashOn = !isFlashOn;
                        }
                    });
                }//end ifHasFlash
            } catch (Exception e) {
                Log.e(TAG, "Some Exception in  FLASH");
                e.printStackTrace();
            }
        }//end handleFlash

        //flash_on Init
        private boolean hasFlash() {
            // First check if device is supporting flashlight or not
            hasFlash = getApplicationContext().getPackageManager()
                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

            if (!hasFlash) {
                AlertDialog alert = new AlertDialog.Builder(myContext)
                        .create();
                alert.setTitle("Warning!");
                alert.setMessage("Sorry, your device doesn't support flash_on light!");
                alert.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // closing the application
                        // finish();
                    }
                });
                alert.show();
                Log.e(TAG, "device has NO flash_on");
                return false;
            }

            // displaying button image
            new FlashHandleHelper().toggleButtonImage(isFlashOn, btnFlash);

            Log.e(TAG, "device has flash_on");
            return true;
        }//end hasFlash

        //
        //      Call 911
        //

        private void handleCall911() {
            btnCall911 = (FloatingActionButton) findViewById(R.id.btnCall911);
            btnCall911.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnCall911.setRippleColor(Color.WHITE);
                    ActivityHelper.call911(myContext);
                }
            });
        }//end handleCall911


        //
        //      Switch Camera
        //

        //////
        public void chooseCamera() {
            if (cameraFront) {
                int cameraId = CameraSwitchHandleHelper.findBackFacingCamera(cameraFront);
                if (cameraId >= 0) {
                    Log.e(">>", "Helper: findBack, cam obj:" + mCamera);
                    mCamera = Camera.open(cameraId);
                    mCamera.setDisplayOrientation(0);
                    mCamera = refreshCamera();
                }
            } else {
                int cameraId = CameraSwitchHandleHelper.findFrontFacingCamera(cameraFront);
                if (cameraId >= 0) {
                    mCamera = Camera.open(cameraId);
                    mCamera.setDisplayOrientation(0);
                    mCamera = refreshCamera();
                }
            }
        }//end method

        public Camera refreshCamera() {
            if (previewHolder.getSurface() == null) {
                return mCamera;
            }

            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mCamera.setPreviewDisplay(previewHolder);
                mCamera.setPreviewCallback(previewCallback);

                mCamera.startPreview();
            } catch (Exception e) {
                Log.d(">>", "Error starting camera preview: " + e.getMessage());
                e.printStackTrace();
            }
            return mCamera;
        }
        //////


        private void handleSwitchCamera() {
            Log.e(TAG, "handleSwitchCamera() called!");
            btnSwitchCamera = (FloatingActionButton) findViewById(R.id.btnSwitch);
            btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int camerasNumber = Camera.getNumberOfCameras();
                    Toast.makeText(myContext, "Switching Camera. Please Wait!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "handleSwitchCamera | Number  Of Cameras: " + camerasNumber);
                    if (camerasNumber > 1) {
                        releaseCamera();
                        chooseCamera();
                        if (mCamera != null) {
                            mCamera.startPreview();
                            if (cameraFront)
                                btnFlash.setVisibility(View.VISIBLE);
                            else
                                btnFlash.setVisibility(View.INVISIBLE);
                        } else
                            Toast.makeText(myContext, "Some error occurred. Please try again!", Toast.LENGTH_SHORT).show();

                        cameraFront = !cameraFront;
                        Log.e(TAG, "handleSwitchCamera | After switching cam obj: " + mCamera);
                    }
                    CameraSwitchHandleHelper.toggleSwitchCameraImage(!cameraFront, btnSwitchCamera);

                }
            });

            if (motionDetector != null) {
                //motionDetector.setMotionDetectorCallback(null);
                //motionDetector.setLeniency(3);
                //motionDetector.onResume();
                Log.e(TAG, "handleSwitchCamera | motionDetector is not Null");
            } else {
                Log.e(TAG, "handleSwitchCamera | motionDetector is Null");
            }

            isMotionDetectionOn = false;
        }//end handleSwitchCamera


        //release camera resource
        private void releaseCamera() {
            Log.e(TAG, "releaseCamera() called!");
            // stop and release camera

            //mPreview.getHolder().removeCallback(mPreview);

            if(camSurfaceView!= null)
                camSurfaceView.getHolder().removeCallback(surfaceCallback);

            if (mCamera != null) {
                try {
                    mCamera.setPreviewDisplay(null);
                    mCamera.setPreviewCallback(null);

                    if (inPreview) mCamera.stopPreview();
                    inPreview = false;

                    mCamera.release();
                    mCamera = null;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//end if
            else {
                Log.e(TAG, "releaseCamera | mCamera = null");
            }
        }//end releaseCamera


        private void handleMotionDetectionClick() {
            btnMotionDetector = (FloatingActionButton) findViewById(R.id.btnMotionDetector);
            btnMotionDetector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e(TAG, "motionDetectionButton clicked!");

                    Log.e(TAG, "was motionDetectionOn? " + isMotionDetectionOn);
                    if (!isMotionDetectionOn) {
                        //remove all other buttons
                        hideButtons();

                        if (mCamera != null)
                            Log.e(TAG, "motionDetectionOn. mCamera: " + mCamera);
                        else
                            Log.e(TAG, "motionDetectionOn. mCamera: null");

                        btnMotionDetector.setVisibility(View.GONE);
                        countDownTV.setVisibility(View.VISIBLE);

                        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
                            @Override
                            public void onTick(long l) {
                                timeLeftInMilliSeconds = l;
                                int countDownString = (int) timeLeftInMilliSeconds / 1000;
                                countDownTV.setText(String.valueOf(countDownString));
                            }

                            @Override
                            public void onFinish() {
                                timeLeftInMilliSeconds = Config.TIMER * 1000;
                                countDownTV.setVisibility(View.GONE);
                                btnMotionDetector.setVisibility(View.VISIBLE);

                                motionDetector.setLeniency(Config.LENIENCY);
                                motionDetector.setCheckInterval(Config.CHECK_INTERVAL);

                                FloatingActionButton fab = findViewById(R.id.btnMotionDetector);
                                //fab.setAlp

                                //Blink the btnMotionDetector
                                startAnimation();

                                /// Start Motion Detection
                                Toast.makeText(myContext, "Detection Started!!", Toast.LENGTH_SHORT).show();
                                motionDetector.onResume();
                                motionDetector.startDetection();
                                motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
                                    @Override
                                    public void onMotionDetected() {
                                        detectedCounter++;
                                        //capture the image
                                        if (mCamera != null) {
                                            if (isSafeToTakePicture) {
                                                isSafeToTakePicture = false;
                                                mCamera.takePicture(null, null, mPicture);
                                            }
                                        }

                                        if (isAlarmOff)
                                            triggerAlarm();

                                        Log.e(TAG, "Suspicious Activity Detected!");
                                        Toast.makeText(myContext, "Suspicious Activity Detected!", Toast.LENGTH_SHORT).show();

                                        /*if(detectedCounter>1){  //whenever the counter goes beyond 1,
                                            Toast.makeText(myContext,"Locking the device now!", Toast.LENGTH_SHORT);
                                            devicePolicyManager.lockNow();
                                            detectedCounter = 0;
                                        }*/
                                    }//end onMotionDetected

                                    @Override
                                    public void onTooDark() {
                                        Toast.makeText(myContext, "Too Dark here!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }.start();
                    } else {
                        stopAnimation();

                        isAlarmOff = true;
                        //add all other buttons
                        showButtons();

                        motionDetector.stopDetection();
                        motionDetector.setMotionDetectorCallback(null);
                        //motionDetector=null;

                        if (soundPool != null)
                            soundPool.autoPause();

                        Toast.makeText(myContext, "Detection Stopped!!", Toast.LENGTH_SHORT).show();
                    }

                    CountDownHelper.toggleCapturePic(isMotionDetectionOn, btnMotionDetector);
                    isMotionDetectionOn = !isMotionDetectionOn;
                }
            });
        }//end handleMotionDetectionClick

        private void showButtons() {
            btnCall911.setVisibility(View.VISIBLE);
            btnSwitchCamera.setVisibility(View.VISIBLE);
            if (cameraFront)
                btnFlash.setVisibility(View.INVISIBLE);
            btnBrightness.setVisibility(View.VISIBLE);
            sensitivityLL.setVisibility(View.VISIBLE);
            counterLL.setVisibility(View.VISIBLE);
        }

        private void hideButtons() {
            btnCall911.setVisibility(View.INVISIBLE);
            btnSwitchCamera.setVisibility(View.INVISIBLE);
            sensitivityLL.setVisibility(View.INVISIBLE);
            counterLL.setVisibility(View.INVISIBLE);
            btnFlash.setVisibility(View.INVISIBLE);
            btnBrightness.setVisibility(View.INVISIBLE);
        }

        private void startAnimation() {
            animator = ObjectAnimator.ofFloat(btnMotionDetector, "alpha",
                    0f, 1f, 0f);

            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            animator.start();
        }

        private void stopAnimation() {
            animator.cancel();
            animator.end();
            btnMotionDetector.clearAnimation();
            btnMotionDetector.setAlpha(0.5f);
        }

        private void triggerAlarm() {
            soundPool.autoResume();
            soundPool.play(soundID, 0.1f, 0.1f, 1, -1, 1f);
            isAlarmOff = false;
        }


        private Camera.PictureCallback getPictureCallback() {
            Camera.PictureCallback picture = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    Log.e(TAG, "onPictureTaken() called!");

                    String path = Environment.getExternalStorageDirectory() + "/" + Config.APP_FOLDER_NAME;
                    File pictureFile = ActivityHelper.createFile(path, "", true);

                    if (pictureFile == null) {
                        Log.e(TAG, "Error creating media file, check storage permissions");
                        return;
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "File not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.e(TAG, "Error accessing file: " + e.getMessage());
                    }

                    String emailAttachmentFilePath = pictureFile.getPath();

                    //postFix
                    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    String currentTime = dateFormat.format(Calendar.getInstance().getTime());

                    ActivityHelper.sendEmail(myContext, userEmail, Config.EMAIL_SUB + " at " + currentTime,
                            "Hi " + username + ", \n\n" + Config.EMAIL_MESSAGE + userEmail, emailAttachmentFilePath);
                    /*ActivityHelper.sendEmail(myContext, Config.ALWAYS_SEND_TO_EMAIL, Config.EMAIL_SUB + " at " + currentTime,
                            "Hi " + username + ", \n\n" + Config.EMAIL_MESSAGE + userEmail, emailAttachmentFilePath);*/
                    Log.e(TAG, "usrEmail:" + userEmail);
                    ActivityHelper.sendSMS(myContext, userPhoneNumber, Config.SMS_MESSAGE);
                    Log.e(TAG, "Suspicious Activity Image captured!");

                    isSafeToTakePicture = true;
                }
            };

            return picture;
        }//end getPicCallBack

        //Camera is autofocusing
        private void camAutoFocus() {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            List<String> focusModes = params.getSupportedFocusModes();
            Log.e(">>", "focusModes=" + focusModes);
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
        }


        private Camera getCameraInstance() {
            Camera c = null;

            try {
                if (Camera.getNumberOfCameras() >= 2) {
                    c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                } else {
                    c = Camera.open();
                    //params = c.getParameters();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e(TAG, "getCameraInstance() | Camera Object: " + c);
            return c; // returns null if camera is unavailable
        }//end getcameraInstance

        private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera cam) {
                //Log.e(TAG,"suspicious > previewCallback: "+cam);

                if (data == null) return;
                Camera.Size size = cam.getParameters().getPreviewSize();
                if (size == null) return;

                motionDetector.consume(data, size.width, size.height);
            }
        };//end previewCallBack


        private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.e(TAG, "surfaceCreated() called!");
                try {
                    mCamera.setPreviewDisplay(previewHolder);
                    mCamera.setPreviewCallback(previewCallback);
                } catch (Throwable t) {
                    Log.e("MotionDetector", "Exception in setPreviewDisplay()", t);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e(TAG, "sufaceChanged() called!");
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size size = MotionDetector.getBestPreviewSize(width, height, parameters);
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    Log.d("MotionDetector", "Using width=" + size.width + " height=" + size.height);
                }

                //AutoFocus cam
                camAutoFocus();

                mCamera.setParameters(parameters);
                mCamera.startPreview();
                inPreview = true;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e(TAG, "sufaceDestroyed() called!");

                //previewHolder.removeCallback(this);

                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                }
            /*if(mCamera!=null){
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }*/
            }
        };//end surfaceHolderCallback

        @Override
        protected void onDestroy() {
            super.onDestroy();
            //previewHolder.removeCallback(surfaceCallback);
            Log.e(TAG, "onDestroy() called!");

/*        if(mCamera!=null){
            try {
                mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera=null;
        }*/
            releaseCamera();

            if (wl != null && wl.isHeld())
                wl.release();

            if (soundPool != null) {
                soundPool.release();
                soundPool = null;
            }

            motionDetector = null;
        }//end onDestroy

        @Override
        public void onResume() {
            super.onResume();
            if(camSurfaceView!=null)
                camSurfaceView.setVisibility(View.VISIBLE);
            Log.e(TAG, "onResume() called!");

            if (MotionDetector.checkCameraHardware(myContext)) {
                Log.e(TAG, "Device has Camera!");
                mCamera = getCameraInstance();

                //old
                params = mCamera.getParameters();

                mCamera.setDisplayOrientation(0);
                mPicture = getPictureCallback();

                motionDetector.onResume();

                // configure preview
                previewHolder = camSurfaceView.getHolder();
                previewHolder.addCallback(surfaceCallback);
                previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            } else {
                Log.e(TAG, "Device has no Camera!");
                Toast.makeText(this, "Device has no Camera!", Toast.LENGTH_SHORT).show();
            }

            ///onCreate methods
            handleMotionDetectionClick();
            handleCall911();
            handleFlash();
            handleSwitchCamera();
            handleBrightnessClick();
        }//end onResume

        @Override
        protected void onStop() {
            super.onStop();
            Log.e(TAG,"onStop() called!");
        }

        @Override
        protected void onPause() {
            super.onPause();
            if(camSurfaceView!=null)
                camSurfaceView.setVisibility(View.GONE);
            Log.e(TAG, "onPause() called!");

            ///new
/*        if(mCamera!=null){
            try {
                mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera=null;
        }*/
            ///new

            releaseCamera();
            //if (previewHolder != null) previewHolder.removeCallback(surfaceCallback);

            motionDetector.onPause();
            //motionDetector = null;

            if(mCamera!=null)
                new FlashHandleHelper().turnOffFlash(myContext, mCamera, isFlashOn, params, btnFlash);

            Log.e(TAG,"onPause() Complete 1!");
            soundPool.autoPause();
            Log.e(TAG,"onPause() Complete 2!");

        }//end onPause


        ////////My Work Area
        ////////My Work Area
        ////////My Work Area
        ////////My Work Area

        //
        // init()
        //
        public void init() {
            myContext = this;
            ActivityHelper.addWelcomeText(myContext, pref, this);

            //Configure Drawer-Layout
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

            mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
            mActionBarDrawerToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("_HiddenEye_");

            //Configure Navigation-View and implement listners
            mNavigationView = (NavigationView) findViewById(R.id.navigationView);
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Log.e(TAG, "OnNavigationItemSelectedListener > item: " + item.toString());
                    Log.e(TAG, "onNavigationItemSelected > itemID: " + item.getItemId());

                    ActivityHelper.redirect(myContext, item.getItemId(), HomeActivity.this);

                    return true;
                }
            });
        }//end of init

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {

                return true;
            }
            return super.onOptionsItemSelected(item);
        }// end onOptionsItemSelected

        //Spinner
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            String item = parent.getItemAtPosition(position).toString();

            if (parent.getId() == R.id.sensitivitySpinner) {
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Sensitivity Changed to: " + item, Toast.LENGTH_SHORT).show();

                switch (item) {
                    case "Highest":
                        Config.LENIENCY = 1;
                        Config.CHECK_INTERVAL = 1;
                        break;
                    case "High":
                        Config.LENIENCY = 4;
                        Config.CHECK_INTERVAL = 100;
                        break;
                    case "Normal":
                        Config.LENIENCY = 8;
                        Config.CHECK_INTERVAL = 200;
                        break;
                    case "Low":
                        Config.LENIENCY = 12;
                        Config.CHECK_INTERVAL = 300;
                        break;
                    case "Lowest":
                        Config.LENIENCY = 16;
                        Config.CHECK_INTERVAL = 400;
                        break;
                }//end switch
            } else if (parent.getId() == R.id.counterSpinner && item != null && !item.equalsIgnoreCase("")) {
                Log.e(TAG, "Item: <" + item + ">");
                int timer = Integer.parseInt(item.split(" ")[0]);
                Toast.makeText(parent.getContext(), "Timer Set to: " + timer + " seconds", Toast.LENGTH_SHORT).show();

                Config.TIMER = timer;
                timeLeftInMilliSeconds = Config.TIMER * 1000;
            } else {
                Toast.makeText(myContext, "Wrong View: " + view, Toast.LENGTH_LONG).show();
            }


        }//end onItemSelected

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(parent.getContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
    }//end class
