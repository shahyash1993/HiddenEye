package com.hiddeneyepro.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hiddeneyepro.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityHelper {
    public static void removeSharedPref(Context context){
        SharedPreferences pref;
        SharedPreferences.Editor editor;

        pref = context.getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }//end removeSharedPref

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

    public static File createFile(String path, String fileName, boolean useTimeStamp){

        if(useTimeStamp)
            fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".jpg";

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
    }//end createFolder


}//end class
