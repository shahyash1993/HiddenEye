package com.hiddeneyepro.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddeneyepro.AboutMeActivity;
import com.hiddeneyepro.ContactMeActivity;
import com.hiddeneyepro.HomeActivity;
import com.hiddeneyepro.HowToUseActivity;
import com.hiddeneyepro.LoginActivity;
import com.hiddeneyepro.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hiddeneyepro.helper.Config.ALWAYS_SEND_TO_EMAIL;

public class ActivityHelper {

    //////////////////////////
    //  When user Logout   //
    /////////////////////////

    public static void logout(Context context, Activity activity){
        removeSharedPref(context);
        switchActivity(context, LoginActivity.class, activity);
/*        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        activity.finish();*/

    }


    public static void switchActivity(Context context, Class destClass, Activity activity){
        Intent intent = new Intent(context, destClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        activity.finish();
    }

    /////////////////////////
    //  Add Welcome Text   //
    ////////////////////////

    public static void addWelcomeText(Context context, SharedPreferences pref, Activity activity){
        //get sharedPref values
        pref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        //String email = pref.getString("email", Config.ADMIN_EMAIL);
        String username= pref.getString("username", Config.ADMIN_USERNAME);

        Log.e(">>","username: "+username);
        //String phoneNumber = pref.getString("phone_number", Config.ADMIN_PHONE_NUMBER);

        //Change nav header
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        /*TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        navUsername.setText("Your Text Here");*/
        TextView welcomeTV;
        welcomeTV = (TextView) headerView.findViewById(R.id.welcomeTV);
        welcomeTV.setText("Welcome "+username+"!");
    }


    ///////////////////////////
    //  Reomve Shared Pref   //
    ///////////////////////////

    public static void removeSharedPref(Context context){
        SharedPreferences pref;
        SharedPreferences.Editor editor;

        pref = context.getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }//end removeSharedPref


    //////////////////////////
    //  Gallery Redirection //
    //////////////////////////

    public static void goToGallery(Context context){
        File path = new File(Environment.getExternalStorageDirectory().getPath()+"/"+Config.APP_FOLDER_NAME+"/");
        Uri uri = Uri.fromFile(path);
        Log.e(">>","uri: "+uri.getPath());
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.sec.android.app.myfiles");
        intent.setAction("samsung.myfiles.intent.action.LAUNCH_MY_FILES");
        intent.putExtra("samsung.myfiles.intent.extra.START_PATH", path.getAbsolutePath());
        context.startActivity(intent);
    }//end goToGallery


    ////////////////////////
    //  Folder Creation   //
    ////////////////////////

    public static boolean createFolder(String path, String folderName){
        path+="/"+folderName;
        File mediaStorageDir = new File(path);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.e( "Folder Creation for", path+" -> Failed!");
            }
            else{
                Log.e("Folder Creation for", path+" -> Successful!");
                return true;
            }
        }
        return false;
    }//end createFolder


    //////////////////////
    //  Create File     //
    //////////////////////
    public static File createFile(String path, String fileName, boolean useTimeStamp){

        if(useTimeStamp)
            fileName = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date())+".jpg";

        path+="/"+fileName;
        File newFile = new File(path);;

        if (!newFile.exists()) {
            try {
                if(newFile.createNewFile()){
                    Log.e("File Creation for", path+" -> Successful!");
                }
                else{
                    Log.e("File Creation for", path+" -> Failed!");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return newFile;
    }//end createFile


    //////////////////////
    //  Call 911        //
    //////////////////////

    public static void call911(Context context){
        String number = "911";
        String uri = "tel:" + number.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }//end call911


    //////////////////////
    //  Send Email      //
    //////////////////////

    public static void sendEmail(Context context, String emailTo, String emailSub, String emailMessage, String emailAttachmentFilePath) {
        //Creating EmailSender object
        EmailSender sm = new EmailSender(context, emailTo, emailSub, emailMessage, emailAttachmentFilePath);
        //Executing sendmail to send email
        sm.execute();
    }//end sendEmail


    //////////////////////
    //  Send SMS        //
    //////////////////////

    public static void sendSMS(Context context, String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            //Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context,ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }//end sendSMS


    //////////////////////////
    //  Toggle Brightness   //
    //////////////////////////

    public static WindowManager.LayoutParams toggleBrightness(boolean isBrightnessHigh, WindowManager.LayoutParams lo_params, FloatingActionButton btnBrightness) {
        if(isBrightnessHigh){
            btnBrightness.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            btnBrightness.setRippleColor(Color.WHITE);
            btnBrightness.setImageResource(R.drawable.brightness_off);
            lo_params.screenBrightness = 0;
        }else{
            btnBrightness.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            btnBrightness.setRippleColor(Color.BLACK);
            btnBrightness.setImageResource(R.drawable.brightness_on);
            lo_params.screenBrightness = 255;
        }
        return lo_params;
    }//end toggleBrightness

    public static void redirect(Context myContext, int itemId, Activity activity) {

        switch(itemId){
            case R.id.nav_home:
                switchActivity(myContext, HomeActivity.class, activity);
//                myContext.startActivity(new Intent(myContext, HomeActivity.class));
                break;
            case R.id.nav_gallery:
                ActivityHelper.goToGallery(myContext);
                break;
            case R.id.nav_howToUse:
                switchActivity(myContext, HowToUseActivity.class, activity);
                //myContext.startActivity(new Intent(myContext, HowToUseActivity.class));
                break;
            case R.id.nav_aboutMe:
                switchActivity(myContext, AboutMeActivity.class, activity);
//                myContext.startActivity(new Intent(myContext, AboutMeActivity.class));
                break;
            case R.id.nav_contactMe:
                switchActivity(myContext, ContactMeActivity.class, activity);
//                myContext.startActivity(new Intent(myContext, ContactMeActivity.class));
                break;
            case R.id.nav_logout:
                ActivityHelper.logout(myContext, activity);
                break;
        }
    }
}//end class
