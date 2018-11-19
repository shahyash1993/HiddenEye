package com.hiddeneyepro;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileIO {
    public static boolean createFolder(String folderName){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),folderName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.e( "Folder Creation>>", "failed to create directory:"+mediaStorageDir.toString());
            }
            else{
                Log.e("Folder Creation>>", "Successfully created directory:"+mediaStorageDir.toString());
                return true;
            }
        }
        return false;
    }//end func
}//end class
