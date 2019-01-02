package com.hiddeneyepro.helper;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;

import com.hiddeneyepro.R;

public class CountDownHelper {
    /*private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = Config.TIMER * 1000;*/

    public static void toggleCapturePic(Boolean isMotionDetectionOn, FloatingActionButton btnMotionDetector){
        if(isMotionDetectionOn) {
            btnMotionDetector.setImageResource(R.drawable.detect_off);
            btnMotionDetector.setRippleColor(Color.WHITE);
            btnMotionDetector.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        }
        else{
            btnMotionDetector.setImageResource(R.drawable.detect_on);
            btnMotionDetector.setRippleColor(Color.BLACK);
            btnMotionDetector.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }


    }//end toggleSwitchCameraImage

    /*public void startTimer(TextView countDownTV, FloatingActionButton btnMotionDetector) {
        countDownTV.setVisibility(View.VISIBLE);
        final TextView countDownTVFinal = countDownTV;
        final FloatingActionButton btnMotionDetectorFinal = btnMotionDetector;

        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliSeconds = l;
                updateTimer(countDownTVFinal);
            }

            @Override
            public void onFinish() {
                countDownTVFinal.setVisibility(View.GONE);
                btnMotionDetectorFinal.setVisibility(View.VISIBLE);
            }
        }.start();
    }*/

/*    private void updateTimer(TextView countDownTV) {
        int countDownString = (int)timeLeftInMilliSeconds/1000;
        countDownTV.setText(countDownString+"");
    }*/
}//end Class
