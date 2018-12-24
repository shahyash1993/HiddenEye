package com.hiddeneyepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hiddeneyepro.helper.ActivityHelper;
import com.hiddeneyepro.helper.Config;

import java.io.File;

import mehdi.sakout.aboutpage.AboutPage;

public class AboutMeActivity extends AppCompatActivity {
    private String TAG =  "AboutMeActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    TextView gitHubLink_tv;
    TextView linkedinLink_tv;

    SharedPreferences pref;

    private NavigationView mNavigationView;
    Context myContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        gitHubLink_tv = findViewById(R.id.gitHubLink_tv);
        linkedinLink_tv = findViewById(R.id.linkedinLink_tv);

        init();

    }//end of onCreate


    public void onGithubLinkClick(View view) {
        goToUrl(((TextView)view).getText().toString());
    }//end of onGitHubClick

    public void onLinkedinLinkClick(View view) {
        goToUrl(((TextView)view).getText().toString());
    }//end of onLinkedinLinkClick

    /*
    *  Goes to specific URL.
    * */
    public void goToUrl(String url){
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }//end of goToUrl

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

                ActivityHelper.redirect(myContext, item.getItemId(), AboutMeActivity.this);

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
