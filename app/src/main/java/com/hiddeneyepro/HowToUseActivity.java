package com.hiddeneyepro;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                //new
                switch(item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(HowToUseActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(HowToUseActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_howToUse:
                        startActivity(new Intent(HowToUseActivity.this, HowToUseActivity.class));
                        break;
                    case R.id.nav_aboutMe:
                        startActivity(new Intent(HowToUseActivity.this, AboutMeActivity.class));
                        break;
                    case R.id.nav_contactMe:
                        startActivity(new Intent(HowToUseActivity.this, ContactMeActivity.class));
                        break;
                    case R.id.nav_logout:
                        ActivityHelper.removeSharedPref(getApplicationContext());
                        startActivity(new Intent(HowToUseActivity.this, LoginActivity.class));
                        break;
                }
                //new

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
