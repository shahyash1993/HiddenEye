package com.hiddeneyepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hiddeneyepro.helper.ActivityHelper;

import mehdi.sakout.aboutpage.AboutPage;

public class ContactMeActivity extends AppCompatActivity {
    String workNo;
    String mobileNo;
    String emailID;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    final String TAG = "ContactMeActivity";

    Context myContext;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);


        workNo = getString(R.string.myWorkNo);
        mobileNo = getString(R.string.myMobileNo);
        emailID = getString(R.string.myEmailID);

        init();
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
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Re: HiddenEye");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "Hi yash,\nI wanted to reach out to you... ");
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
    }//end sendMsg

    //init()
    public void init(){
        ActivityHelper.addWelcomeText(myContext,pref,this);

        //Configure Drawer-Layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("_HiddenEye_");

        //Configure Navigation-View and implement listners
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.e(TAG,"OnNavigationItemSelectedListener > item: "+item.toString());
                Log.e(TAG,"onNavigationItemSelected > itemID: "+item.getItemId());

                ActivityHelper.redirect(myContext, item.getItemId(), ContactMeActivity.this);

                return true;
            }
        });
    }//end of init

    /*
     *  When the item from options is selected.
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }// end onOptionsItemSelected


}//end class