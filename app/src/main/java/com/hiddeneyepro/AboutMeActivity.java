package com.hiddeneyepro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import mehdi.sakout.aboutpage.AboutPage;

public class AboutMeActivity extends AppCompatActivity {
    private String TAG =  "AboutMeActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    TextView gitHubLink_tv;
    TextView linkedinLink_tv;

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                        startActivity(new Intent(AboutMeActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(AboutMeActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_howToUse:
                        startActivity(new Intent(AboutMeActivity.this, HowToUseActivity.class));
                        break;
                    case R.id.nav_aboutMe:
                        startActivity(new Intent(AboutMeActivity.this, AboutMeActivity.class));
                        break;
                    case R.id.nav_contactMe:
                        startActivity(new Intent(AboutMeActivity.this, ContactMeActivity.class));
                        break;
                    case R.id.nav_logout:
                        startActivity(new Intent(AboutMeActivity.this, LoginActivity.class));
                        break;
                }
                //new

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
