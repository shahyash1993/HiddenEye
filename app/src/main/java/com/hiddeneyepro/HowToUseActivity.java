package com.hiddeneyepro;

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
import android.widget.MediaController;
import android.widget.VideoView;

import com.hiddeneyepro.helper.ActivityHelper;

public class HowToUseActivity extends AppCompatActivity {

    private String TAG =  "HowToActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;

    VideoView videoView;
    MediaController m;
    public Context myContext;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"onCreate() called!");
        myContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        init();
        videoView = (VideoView) findViewById(R.id.howToUse_vv);
        playVideo();

    }//end onCreate

    private void playVideo(){
        String uriPath = "android.resource://" + getPackageName() +"/"+R.raw.how_to_use;

        Uri uri2 = Uri.parse(uriPath);
        videoView.setVideoURI(uri2);
        videoView.requestFocus();
        videoView.start();
    }

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

                ActivityHelper.redirect(myContext, item.getItemId(), HowToUseActivity.this);

                return true;
            }
        });
    }//end of init

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }// end onOptionsItemSelected
}//end class
