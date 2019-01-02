package com.hiddeneyepro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hiddeneyepro.helper.Config;

import static com.hiddeneyepro.helper.Config.PERMISSIONS_CHECKED;

public class PermissionCheckerActivity extends AppCompatActivity {
    private final String TAG = Config.TAG+getClass().getSimpleName()+">>";
    //Permissions
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 3;

    Context myContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myContext = this;
        Log.e(TAG,"Permissions are being checked!");
        Toast.makeText(myContext, "Permissions are being checked!", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        checkPermissions();
        Config.PERMISSIONS_CHECKED = true;
        goToHome();

    }//end onCreate

    //Check Permissions
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.e(TAG,"Camera Permission is not granted yet!!!");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Log.e(TAG,"Camera Permission granted!");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }//end of CAMERA permission

        //SMS permission
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"Send SMS Permission is not granted yet!!!");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {
                Log.e(TAG,"Send SMS Permission granted!");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }//end of SMS Permission

        //Write external Storage permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"Write External Storage Permission is not granted!!!");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }//end

        //Read external Storage permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"Write External Storage Permission is not granted!!!");


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }//end checkPermissions

    private void goToHome() {
        startActivity(new Intent(this,HomeActivity.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(myContext, "Camera Permission Given!",Toast.LENGTH_SHORT).show();

                } else {
                    Log.e(TAG,"Permission was denied by the user!");
                    Toast.makeText(myContext, "Camera Permission was Denied!",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "SMS Permission Granted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(myContext, "External Storage Permission Granted!",Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG,"Permission was denied by the user!");
                    Toast.makeText(myContext, "External Storage Permission was Denied!",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(myContext, "External Storage Permission Granted!",Toast.LENGTH_SHORT).show();
                    //go to MAIN Activity
                } else {
                    Log.e(TAG,"Permission was denied by the user!");
                    Toast.makeText(myContext, "External Storage Permission was Denied!",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }//end checking permission

    @Override
    protected void onResume() {
        super.onResume();
        if(PERMISSIONS_CHECKED)
            goToHome();
    }//end onResume
}//end PermissionChecker
