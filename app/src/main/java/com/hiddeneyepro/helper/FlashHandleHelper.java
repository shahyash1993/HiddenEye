package com.hiddeneyepro.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;

import com.hiddeneyepro.R;


public class FlashHandleHelper {
    MediaPlayer mp;
    public void turnOnFlash(Context context, Camera mCamera, Boolean isFlashOn, Camera.Parameters params, FloatingActionButton btnFlash) {
        if (!isFlashOn) {
            if (mCamera == null || params == null) {
                return;
            }
            // play sound
            playSound(context, isFlashOn);

            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
            mCamera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage(isFlashOn, btnFlash);
        }

    }

    public void turnOffFlash(Context context, Camera mCamera, Boolean isFlashOn, Camera.Parameters params, FloatingActionButton btnFlash) {
        if (isFlashOn) {
            if (mCamera == null || params == null) {
                return;
            }
            // play sound
            playSound(context, isFlashOn);

            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            //mCamera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage(isFlashOn, btnFlash);
        }
    }

    // Playing sound
    public void playSound(Context context, Boolean isFlashOn){
        if(isFlashOn){
            mp = MediaPlayer.create(context, R.raw.flash_on);
        }else{
            mp = MediaPlayer.create(context, R.raw.flash_on);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }

    /*
     * Toggle switch button images
     * changing image states to on / off
     * */
    public void toggleButtonImage(Boolean isFlashOn, FloatingActionButton btnSwitch){
        if(isFlashOn){
            btnSwitch.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            btnSwitch.setRippleColor(Color.BLACK);
            btnSwitch.setImageResource(R.drawable.flash_on);

        }else{
            btnSwitch.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            btnSwitch.setRippleColor(Color.WHITE);
            btnSwitch.setImageResource(R.drawable.flash_off);
        }
    }
}
