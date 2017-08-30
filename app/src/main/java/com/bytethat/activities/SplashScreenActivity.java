/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bytethat.R;
import com.bytethat.database.DatabaseHelper;

/**
 * @author TCASHAVELLY
 *
 * Copyright 2017 Thomas Cashavelly
 * All Rights Reserved
 */
public class SplashScreenActivity extends Activity {

    private static final int SPLASH_DISPLAY_LENGTH = 4000;
    DatabaseHelper db;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        db = new DatabaseHelper(getApplicationContext());

            if (!isMyServiceRunning()) {
                super.onCreate(icicle);
                setContentView(R.layout.splashscreen);

            /* New Handler to start the Menu-Activity 
             * and close this Splash-Screen after some seconds.*/

                if(db.soundboardExists()) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            SplashScreenActivity.this.startActivity(mainIntent);
                            SplashScreenActivity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }else{
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                    /* Create an Intent that will start the Wizard-Activity. */
                            Intent mainIntent = new Intent(SplashScreenActivity.this, WizardActivity.class);
                            SplashScreenActivity.this.startActivity(mainIntent);
                            SplashScreenActivity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }

            } else {

                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                this.startActivity(mainIntent);

            }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.bytethat".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}