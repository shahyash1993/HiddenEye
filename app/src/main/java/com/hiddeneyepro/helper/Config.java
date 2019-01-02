package com.hiddeneyepro.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

    public static int LENIENCY = 1;
    public static int CHECK_INTERVAL = 1;
    public static final String APP_NAME = "HiddenEye";
    public static final String SHARED_PREF_NAME = "HiddenEyePref";
    public static final String REST_URL = "http://172.20.10.3:8080/HiddenEye/rest/DBConnection";
    //public static final String REST_URL = "http://192.168.0.6:8080/HiddenEye/rest/DBConnection";

    public static final String APP_FOLDER_NAME= "HiddenEye";

    public static final String TAG= "HiddenEye::";

//    public static final String ADMIN_EMAIL= "paragshah157@gmail.com";
    public static final String ADMIN_EMAIL= "shahyash_1993@yahoo.com";
    public static final String ALWAYS_SEND_TO_EMAIL= "ypshah.13@gmail.com";

    public static final String ADMIN_PHONE_NUMBER= "+919427065166";
    public static final String ADMIN_USERNAME= "admin_username";


    public static final String EMAIL_MESSAGE = "HiddenEye has detected Suspicious activities during the Theft Detection. " +
            "Please find the attached Suspicious Image. " +
            "Please do not reply to this email - we are unable to respond to messages at this email. " +
            "\n\nIf the following email does not apply to you please disregard." +
            "\nThis email was sent to: ";
    public static final String EMAIL_SUB = "Suspicious Activity detected";

    public static final String EMAIL_FROM = "noreply.hiddeneye@gmail.com";
    public static final String EMAIL_FROM_PASSWORD = "Aeroplane1";

    public static final String SMS_MESSAGE = "Suspected Activity Detected. Please check your email for Detected images.";
    public static int TIMER = 13;

    public static final String WAKE_LOCK_NAME = APP_NAME+"::"+"TheftDetectionWakeLock";
    public static Boolean PERMISSIONS_CHECKED = false;


}//end class

