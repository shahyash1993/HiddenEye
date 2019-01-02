package com.hiddeneyepro.helper;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyAdminLocker extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, "Device Admin Permission Granted!", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "Device Admin Permission Granted!", Toast.LENGTH_SHORT).show();
    }//end onEnabled

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Device Admin Permission Denied!", Toast.LENGTH_SHORT).show();
    }//end onDisabled
}
