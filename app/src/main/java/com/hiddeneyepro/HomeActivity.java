package com.hiddeneyepro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

public class HomeActivity extends AppCompatActivity{

    private String TAG =  "HomeActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolBar;

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("_HiddenEye_");


        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Log.e(TAG,"OnNavigationItemSelectedListener > item: "+item.toString());
                    Log.e(TAG,"onNavigationItemSelected > itemID: "+item.getItemId());

                    //new
                    switch(item.getItemId()){
                        case R.id.nav_home:
                            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                            break;
                        case R.id.nav_settings:
                            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                            break;
                        case R.id.nav_howToUse:
                            startActivity(new Intent(HomeActivity.this, HowToUseActivity.class));
                            break;
                        case R.id.nav_aboutMe:
                            startActivity(new Intent(HomeActivity.this, AboutMeActivity.class));
                            break;
                        case R.id.nav_contactMe:
                            startActivity(new Intent(HomeActivity.this, ContactMeActivity.class));
                            break;
                        case R.id.nav_logout:
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            break;
                    }
                    //new

                    return true;
                }
            });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if(mActionBarDrawerToggle.onOptionsItemSelected(item)) {

                return true;
            }
        return super.onOptionsItemSelected(item);
    }// end onOptionsItemSelected


}
