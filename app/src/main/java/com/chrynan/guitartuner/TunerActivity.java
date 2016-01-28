package com.chrynan.guitartuner;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.chrynan.guitartuner.util.PermissionUtils;

import java.util.Calendar;

/*
 * Copyright 2016 chRyNaN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Created by chRyNaN on 1/18/2016.
 * Circular Reveal Animation Transition help from here: https://gist.github.com/ferdy182/d9b3525aa65b5b4c468a
 */
public class TunerActivity extends AppCompatActivity {
    private static final String TAG = TunerActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TunerFragment tunerFragment;
    private PitchFragment pitchFragment;
    private boolean showCancel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        showCancel = false;
        setContentView(R.layout.tuner_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tunerFragment = new TunerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, tunerFragment, TunerFragment.TAG).commit();
        setTimedNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if(showCancel) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if(showCancel){
                    transitionBackToTunerFragment(pitchFragment.unreveal(pitchFragment.getX(), pitchFragment.getY()));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case TunerFragment.AUDIO_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(tunerFragment != null) {
                        tunerFragment.start();
                    }
                }
                break;
        }
    }

    public void transitionToPitchFragment(Note note, float x, float y){
        pitchFragment = PitchFragment.newInstance(note, x, y);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pitchFragment, PitchFragment.TAG).commit();
        tunerFragment.stop();
        showCancel = true;
        invalidateOptionsMenu();
    }

    public void transitionBackToTunerFragment(Animator anim){
        if(anim != null){
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // remove the fragment only when the animation finishes
                    getSupportFragmentManager().beginTransaction().remove(pitchFragment).commit();
                    //to prevent flashing the fragment before removing it, execute pending transactions inmediately
                    getSupportFragmentManager().executePendingTransactions();
                    tunerFragment.start();
                    showCancel = false;
                    invalidateOptionsMenu();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.start();
        }else{
            getSupportFragmentManager().beginTransaction().remove(pitchFragment).commit();
            tunerFragment.start();
            showCancel = false;
            invalidateOptionsMenu();
        }
    }

    private void setTimedNotification(){
        //Send a notification to the user reminding them to tune their guitar if they haven't opened the app in awhile
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationPublishReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, NotificationPublishReceiver.REQUEST_CODE, intent, 0);
        //Cancel any previously set alarms
        alarmManager.cancel(alarmIntent);
        //One week - 7 days, 24 hours, 60 minutes, 60 seconds, 1000 milliseconds
        long time = Calendar.getInstance().getTimeInMillis() + (7 * 24 * 60 * 60 * 1000);
        alarmManager.set(AlarmManager.RTC, time, alarmIntent);
    }

}
