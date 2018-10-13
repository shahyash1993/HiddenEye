package com.hiddeneyepro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;

public class ContactMeActivity extends Activity {
    String workNo;
    String mobileNo;
    String emailID;
    final String TAG = "ContactMeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);


        workNo = getString(R.string.myWorkNo);
        mobileNo = getString(R.string.myMobileNo);
        emailID = getString(R.string.myEmailID);


    }//end of onCreate

    public void onWorkClick(View view) {
        makeCall(workNo);
    }//onWorkClick

    private void makeCall(String phoneNo) {
        String uri = "tel:" + phoneNo.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }//end makeCall

    public void onMobileClick(View view) {
        makeCall(mobileNo);
    }//end onMobileClick

    public void onEmailClick(View view) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ emailID });
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello Yash | HiddenEye");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "I wanted to reach out to you as");
        startActivity(Intent.createChooser(i, "Send email"));

    }//end onEmailClick

    public void onWorkMsgClick(View view) {
        sendMsg(workNo);
    }//onWorkMsgClick

    public void onMobileMsgClick(View view) {
        sendMsg(mobileNo);
    }//onWorkMsgClick

    public void sendMsg(String phoneNo){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        Toast.makeText(this,"SMS to>"+phoneNo,Toast.LENGTH_LONG).show();

        sendIntent.setData(Uri.parse("smsto:"+phoneNo));
        sendIntent.putExtra("sms_body", "Hi Yash");
        startActivity(sendIntent);
    }


}//end class