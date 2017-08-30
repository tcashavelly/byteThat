/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bytethat.R;

/**
 * @author TCASHAVELLY
 *         <p>
 *         Copyright 2017 Thomas Cashavelly
 *         All Rights Reserved
 */
public class WizardActivity extends Activity {


    private static final int SPLASH_DISPLAY_LENGTH = 4000;
    public int step = 0;
    ImageView wizardScreen;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        wizardScreen = (ImageView) findViewById(R.id.splashscreen);
        wizardScreen.setImageResource(R.drawable.wizard0);
        wizardScreen.setOnClickListener(nextWizardStep);

    }

    private View.OnClickListener nextWizardStep = new View.OnClickListener() {

        public void onClick(View arg0) {
            if (step == 0) {
                wizardScreen.setImageResource(R.drawable.wizard1);
                step++;
            } else if (step == 1) {
                wizardScreen.setImageResource(R.drawable.wizard2);
                step++;
            } else if (step == 2) {
                Intent mainIntent = new Intent(WizardActivity.this, MainActivity.class);
                WizardActivity.this.startActivity(mainIntent);
                WizardActivity.this.finish();
                step = 0;
            }
        }
    };


}